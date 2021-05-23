package com.kairachka.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.controller.OperationController;
import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.OperationRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.OperationRepository;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.Impl.BillServiceImpl;
import com.kairachka.bankapi.service.Impl.OperationServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.Authenticator;
import com.sun.net.httpserver.HttpServer;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();
    private static final BillRepository billRepository = new BillRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final OperationRepository operationRepository = new OperationRepositoryImpl();
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BillServiceImpl billService = new BillServiceImpl(billRepository, userService);
    private final OperationServiceImpl operationService = new OperationServiceImpl(operationRepository, billService, userService);
    private static final String url = "jdbc:h2:mem:testIntegration;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/CreateTestTables";
    private static final String deleteTable = "src/test/resources/SQLScripts/DropTestTables";
    private static final String createAdminUser = "src/test/resources/SQLScripts/CreateAdminUser";
    private static final String createUser = "src/test/resources/SQLScripts/CreateUser";
    private static final String createUser2 = "src/test/resources/SQLScripts/CreateUser2";
    private static Connection connectionDB;
    private final OperationController operationController = new OperationController(operationService, userService);
    private final Authenticator authenticator = new Authenticator(userService);
    private static final String admin = "Basic YWRtaW46YWRtaW4=";
    private static final String user = "Basic dXNlcjphZG1pbg==";
    private static final String user2 = "Basic cXdlOmFkbWlu";
    private static final String userNotFound = "Basic cXdlcnR5OmFkbWlu";

    @BeforeEach
    void setUp() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        assert server != null;
        server.createContext("/api/test/operation", operationController).setAuthenticator(authenticator);
        server.start();
        connectionDB = DriverManager.getConnection(url);
        billRepository.setUrl(url);
        userRepository.setUrl(url);
        operationRepository.setUrl(url);
        RunScript.execute(connectionDB, new FileReader(createTable));
        RunScript.execute(connectionDB, new FileReader(createAdminUser));
        RunScript.execute(connectionDB, new FileReader(createUser));
        RunScript.execute(connectionDB, new FileReader(createUser2));
    }

    @AfterEach
    void shutDown() throws SQLException, FileNotFoundException {
        server.stop(0);
        RunScript.execute(connectionDB, new FileReader(deleteTable));
        connectionDB.close();
    }

    @Test
    void handleGetUserBillId() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        PreparedStatement operation1 =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 100 )");
        PreparedStatement operation2 =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 200 )");
        operation1.execute();
        operation2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetUserOperationNotFound() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetUserBillNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetUserUserNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", userNotFound);
        assertEquals(connection.getResponseCode(), 401);
    }

    @Test
    void handleGetUserNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        PreparedStatement operation1 =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 100 )");
        PreparedStatement operation2 =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 200 )");
        operation1.execute();
        operation2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user2);
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleGetUserAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetEmployeeEmpty() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetEmployeeStatus() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?status=active");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetEmployeeAny404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePost201() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID, BALANCE) VALUES (2, 1000)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Operation operation = new Operation(1, 1, 100);
        String jsonRequest = mapper.writeValueAsString(operation);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 201);
    }

    @Test
    void handlePost406() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID, BALANCE) VALUES (2, 1000)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Operation operation = new Operation(1, 2, 100);
        String jsonRequest = mapper.writeValueAsString(operation);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 406);
    }
    @Test
    void handlePost404() throws IOException, SQLException {
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Operation operation = new Operation(1, 1, 100);
        String jsonRequest = mapper.writeValueAsString(operation);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePost403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        Operation operation = new Operation();
        String jsonRequest = mapper.writeValueAsString(operation);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handlePostAny404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Operation operation = new Operation(1, 1, 100);
        String jsonRequest = mapper.writeValueAsString(operation);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePUT200() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID, BALANCE) VALUES (2, 1000)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        PreparedStatement operation =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 100 )");
        operation.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation?id=1&action=approved");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handlePUT406() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID, BALANCE) VALUES (2, 1000)");
        addBill.execute();
        PreparedStatement partner =
                connectionDB.prepareStatement(
                        "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( 'K', 2000000000000000002 )");
        partner.execute();
        PreparedStatement operation =
                connectionDB.prepareStatement(
                        "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( 1, 1, 100 )");
        operation.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/operation?id=1&action=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 406);
    }

    @Test
    void handlePUTOperationNotFound() throws IOException, OperationNotFoundException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?id=1&action=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePUT404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation?qwe=1&action=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/operation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 405);
    }

}
