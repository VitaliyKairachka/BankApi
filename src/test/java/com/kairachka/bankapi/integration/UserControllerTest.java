package com.kairachka.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.controller.UserController;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.UserRepository;
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
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private static final String url = "jdbc:h2:mem:testIntegration;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/CreateTestTables.sql";
    private static final String deleteTable = "src/test/resources/SQLScripts/DropTestTables.sql";
    private static final String createAdminUser = "src/test/resources/SQLScripts/CreateAdminUser.sql";
    private static final String createUser = "src/test/resources/SQLScripts/CreateUser.sql";
    private static final String createUser2 = "src/test/resources/SQLScripts/CreateUser2.sql";
    private static Connection connectionDB;
    private final UserController userController = new UserController(userService);
    private final Authenticator authenticator = new Authenticator(userService);
    private static final String admin = "Basic YWRtaW46YWRtaW4=";
    private static final String user = "Basic dXNlcjphZG1pbg==";


    @BeforeEach
    void setUp() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        assert server != null;
        server.createContext("/api/test/user", userController).setAuthenticator(authenticator);
        server.start();
        connectionDB = DriverManager.getConnection(url);
        userRepository.setUrl(url);
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
    void handle() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        User user = new User("123", "123", "V",
                "K", "A", "123", "123", null);
        String jsonRequest = mapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(201, connection.getResponseCode());
    }

    @Test
    void handle406() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        User user = new User();
        String jsonRequest = mapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(406, connection.getResponseCode());
    }

    @Test
    void handle404() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", admin);
        connection.setDoOutput(true);
        User user = new User();
        String jsonRequest = mapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(404, connection.getResponseCode());
    }
    @Test
    void handle403() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user);
        connection.setDoOutput(true);
        User user = new User("123", "123", "V",
                "K", "A", "123", "123", null);
        String jsonRequest = mapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        assertEquals(403, connection.getResponseCode());
    }

    @Test
    void handle405() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", admin);
        assertEquals(405, connection.getResponseCode());
    }
}
