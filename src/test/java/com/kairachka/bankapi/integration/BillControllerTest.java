package com.kairachka.bankapi.integration;

import com.kairachka.bankapi.controller.BillController;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.Impl.BillServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.Authenticator;
import com.sun.net.httpserver.HttpServer;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

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


import static org.junit.jupiter.api.Assertions.*;


public class BillControllerTest {
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();
    private static final BillRepository billRepository = new BillRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BillServiceImpl billService = new BillServiceImpl(billRepository, userService);
    private static final String url = "jdbc:h2:mem:testIntegration;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/CreateTestTables";
    private static final String deleteTable = "src/test/resources/SQLScripts/DropTestTables";
    private static final String createAdminUser = "src/test/resources/SQLScripts/CreateAdminUser";
    private static final String createUser = "src/test/resources/SQLScripts/CreateUser";
    private static Connection connectionDB;
    private final BillController billController = new BillController(billService, userService);
    private final Authenticator authenticator = new Authenticator(userService);

    @BeforeEach
    void setUp() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        assert server != null;
        server.createContext("/api/test/bill", billController).setAuthenticator(authenticator);
        server.start();
        connectionDB = DriverManager.getConnection(url);
        billRepository.setUrl(url);
        userRepository.setUrl(url);
        RunScript.execute(connectionDB, new FileReader(createTable));
        RunScript.execute(connectionDB, new FileReader(createAdminUser));
        RunScript.execute(connectionDB, new FileReader(createUser));
    }

    @AfterEach
    void shutDown() throws SQLException, FileNotFoundException {
        server.stop(0);
        RunScript.execute(connectionDB, new FileReader(deleteTable));
        connectionDB.close();
    }

    @Test
    void handleEmpty() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/bill");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetId() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/bill?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetIdNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/bill?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleGetIdBillNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetBillId() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/bill?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    void handleGetBillIdBillNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGetBillIdBillNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/bill?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleGetAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handleGet403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handlePostId201() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        connection.setDoOutput(true);
        assertEquals(connection.getResponseCode(), 201);
    }

    @Test
    void handlePostId406() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?id=3");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        connection.setDoOutput(true);
        assertEquals(connection.getResponseCode(), 406);
    }

    @Test
    void handlePost404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handlePost403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handleAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/bill");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(connection.getResponseCode(), 405);
    }

}
