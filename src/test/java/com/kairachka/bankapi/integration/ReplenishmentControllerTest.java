package com.kairachka.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.controller.ReplenishmentController;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.ReplenishmentRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.ReplenishmentRepository;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.Impl.BillServiceImpl;
import com.kairachka.bankapi.service.Impl.ReplenishmentServiceImpl;
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

public class ReplenishmentControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();
    private static final BillRepository billRepository = new BillRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final ReplenishmentRepository replenishmentRepository = new ReplenishmentRepositoryImpl();
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BillServiceImpl billService = new BillServiceImpl(billRepository, userService);
    private final ReplenishmentServiceImpl replenishmentService =
            new ReplenishmentServiceImpl(replenishmentRepository, billService, userService);
    private static final String url = "jdbc:h2:mem:testIntegration;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/CreateTestTables";
    private static final String deleteTable = "src/test/resources/SQLScripts/DropTestTables";
    private static final String createAdminUser = "src/test/resources/SQLScripts/CreateAdminUser";
    private static final String createUser = "src/test/resources/SQLScripts/CreateUser";
    private static final String createUser2 = "src/test/resources/SQLScripts/CreateUser2";
    private static Connection connectionDB;
    private final ReplenishmentController replenishmentController =
            new ReplenishmentController(replenishmentService, userService);
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
        server.createContext("/api/test/replenishment", replenishmentController).setAuthenticator(authenticator);
        server.start();
        connectionDB = DriverManager.getConnection(url);
        billRepository.setUrl(url);
        userRepository.setUrl(url);
        replenishmentRepository.setUrl(url);
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
    void handleGet200() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement replenishment1 =
                connectionDB.prepareStatement("INSERT INTO REPLENISHMENTS (SUM, BILL_ID) VALUES ( 100, 1 )");
        PreparedStatement replenishment2 =
                connectionDB.prepareStatement("INSERT INTO REPLENISHMENTS (SUM, BILL_ID) VALUES ( 200, 1 )");
        replenishment1.execute();
        replenishment2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetBillNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetUserNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", userNotFound);
        assertEquals(connection.getResponseCode(), 401);
    }

    @Test
    void handleGetNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement replenishment1 =
                connectionDB.prepareStatement("INSERT INTO REPLENISHMENTS (SUM, BILL_ID) VALUES ( 100, 1 )");
        PreparedStatement replenishment2 =
                connectionDB.prepareStatement("INSERT INTO REPLENISHMENTS (SUM, BILL_ID) VALUES ( 200, 1 )");
        replenishment1.execute();
        replenishment2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user2);
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleGet404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGet403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handlePost201() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Replenishment replenishment = new Replenishment(100, 1);
        String jsonRequest = mapper.writeValueAsString(replenishment);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 201);
    }

    @Test
    void handlePost406() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        Replenishment replenishment = new Replenishment(100, 2);
        String jsonRequest = mapper.writeValueAsString(replenishment);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(connection.getResponseCode(), 406);
    }

    @Test
    void handlePost404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePost403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/replenishment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", user);
        assertEquals(connection.getResponseCode(), 405);
    }

}
