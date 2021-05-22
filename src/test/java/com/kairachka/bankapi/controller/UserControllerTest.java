package com.kairachka.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.Authenticator;
import com.kairachka.bankapi.util.Server;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserControllerTest {
    @Mock
    Authenticator authenticator = new Authenticator("realm");
    @Mock
    UserServiceImpl userService;
    UserController userController = new UserController(userService);
    ObjectMapper objectMapper = new ObjectMapper();
    HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(8090), 0);
        assert server != null;
        server.createContext("/api/test/user", userController).setAuthenticator(authenticator);
        server.start();
    }

    @AfterEach
    void shutDown() {
        server.stop(0);
    }

    @Test
    void handle() throws IOException {
        URL url = new URL("http://localhost:8090/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        String jsonRequest = objectMapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        Mockito.when(authenticator.checkCredentials(anyString(), anyString())).thenReturn(true);
        Assertions.assertEquals(connection.getResponseCode(), 200);
    }
}