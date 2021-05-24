package com.kairachka.bankapi.integration;

import com.kairachka.bankapi.controller.CardController;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.repository.CardRepository;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.CardRepositoryImpl;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.Impl.BillServiceImpl;
import com.kairachka.bankapi.service.Impl.CardServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.Authenticator;
import com.sun.net.httpserver.HttpServer;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardControllerTest {
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();
    private static final BillRepository billRepository = new BillRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final CardRepository cardRepository = new CardRepositoryImpl();
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BillServiceImpl billService = new BillServiceImpl(billRepository, userService);
    private final CardServiceImpl cardService = new CardServiceImpl(cardRepository, userService, billService);
    private static final String url = "jdbc:h2:mem:testIntegration;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/CreateTestTables.sql";
    private static final String deleteTable = "src/test/resources/SQLScripts/DropTestTables.sql";
    private static final String createAdminUser = "src/test/resources/SQLScripts/CreateAdminUser.sql";
    private static final String createUser = "src/test/resources/SQLScripts/CreateUser.sql";
    private static final String createUser2 = "src/test/resources/SQLScripts/CreateUser2.sql";
    private static Connection connectionDB;
    private final CardController cardController = new CardController(userService, cardService);
    private final Authenticator authenticator = new Authenticator(userService);
    private static final String admin = "Basic YWRtaW46YWRtaW4=";
    private static final String user = "Basic dXNlcjphZG1pbg==";
    private static final String user2 = "Basic cXdlOmFkbWlu";

    @BeforeEach
    void setUp() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        assert server != null;
        server.createContext("/api/test/card", cardController).setAuthenticator(authenticator);
        server.start();
        connectionDB = DriverManager.getConnection(url);
        billRepository.setUrl(url);
        userRepository.setUrl(url);
        cardRepository.setUrl(url);
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
    void handleGetBillId200() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement card1 =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '01/01', 'V', 'K', 1 )");
        PreparedStatement card2 =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '02/02', 'Q', 'W', 1 )");
        card1.execute();
        card2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    void handleGetBillId404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic dXNlcjphZG1pbg==");
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handleGetBillIdNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement card1 =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '01/01', 'V', 'K', 1 )");
        PreparedStatement card2 =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '02/02', 'Q', 'W', 1 )");
        card1.execute();
        card2.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cXdlOmFkbWlu");
        assertEquals(403, connection.getResponseCode());
    }

    @Test
    void handleUserGetBillIdBillNotFound() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handleUserGetId200() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement card =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '01/01', 'V', 'K', 1 )");
        card.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    void handleUserGetIdCardNotFound() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handleUserGetIdBillNotFound() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handleUserGetIdNoAccess() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (3)");
        addBill.execute();
        PreparedStatement card =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '01/01', 'V', 'K', 1 )");
        card.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(403, connection.getResponseCode());
    }

    @Test
    void handleUserGetAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", user);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handleEmployeeGetStatus200() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?status=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    void handleEmployeeGetEmpty200() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    void handleEmployeeGetAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handlePostBillId201() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        assertEquals(201, connection.getResponseCode());
    }

    @Test
    void handlePostBillId406() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?billId=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        assertEquals(406, connection.getResponseCode());
    }

    @Test
    void handlePostBillId404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handlePost403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?qwe=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        assertEquals(403, connection.getResponseCode());
    }

    @Test
    void handlePut200() throws IOException, SQLException {
        PreparedStatement addBill = connectionDB.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (2)");
        addBill.execute();
        PreparedStatement card =
                connectionDB.prepareStatement("INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) " +
                        "VALUES ( '01/01', 'V', 'K', 1 )");
        card.execute();
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1&action=active");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    void handlePut406() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?id=1&action=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(406, connection.getResponseCode());
    }

    @Test
    void handlePut404() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card?qwe=");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    void handlePut403() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", user);
        assertEquals(403, connection.getResponseCode());
    }

    @Test
    void handleAny() throws IOException {
        URL url = new URL("http://localhost:" + port + "/api/test/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", user);
        assertEquals(405, connection.getResponseCode());
    }

}
