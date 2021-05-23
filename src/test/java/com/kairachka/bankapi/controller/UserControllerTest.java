package com.kairachka.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.Authenticator;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;


import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    @Mock
    private final UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);
    @InjectMocks
    private Authenticator authenticator;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer server;
    private final int port = new InetSocketAddress((int) (Math.random() * 65535)).getPort();

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        assert server != null;
        server.createContext("/api/test/user", userController).setAuthenticator(authenticator);
        server.start();
    }

    @AfterEach
    void shutDown() {
        server.stop(0);
    }

    @Test
    void handle() throws IOException, UserNotFoundException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic MTIzOjEyMw==");
        connection.setDoOutput(true);
        User user = new User("123", "123", "V",
                "K", "A", "123", "123", null);
        String jsonRequest = objectMapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        Mockito.when(userService.authentication(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(userService.getRoleByLogin(Mockito.anyString())).thenReturn(Role.EMPLOYEE);
        Mockito.when(userService.addUser(Mockito.any())).thenReturn(true);
        assertEquals(connection.getResponseCode(), 201);
    }

    @Test
    void handle406() throws IOException, UserNotFoundException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic MTIzOjEyMw==");
        connection.setDoOutput(true);
        User user = new User("123", "123", "V",
                "K", "A", "123", "123", null);
        String jsonRequest = objectMapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        Mockito.when(userService.authentication(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(userService.getRoleByLogin(Mockito.anyString())).thenReturn(Role.EMPLOYEE);
        Mockito.when(userService.addUser(Mockito.any())).thenReturn(false);
        assertEquals(connection.getResponseCode(), 406);
    }

    @Test
    void handle404() throws IOException, UserNotFoundException {
        URL url = new URL("http://localhost:"+port+"/api/test/user?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic MTIzOjEyMw==");
        connection.setDoOutput(true);
        User user = new User();
        String jsonRequest = objectMapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        Mockito.when(userService.authentication(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(userService.getRoleByLogin(Mockito.anyString())).thenReturn(Role.EMPLOYEE);
        assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void handle403() throws IOException, UserNotFoundException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic MTIzOjEyMw==");
        connection.setDoOutput(true);
        User user = new User("123", "123", "V",
                "K", "A", "123", "123", null);
        String jsonRequest = objectMapper.writeValueAsString(user);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(jsonRequest);
        out.flush();
        out.close();
        Mockito.when(userService.authentication(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(userService.getRoleByLogin(Mockito.anyString())).thenReturn(Role.USER);
        assertEquals(connection.getResponseCode(), 403);
    }

    @Test
    void handle405() throws IOException {
        URL url = new URL("http://localhost:"+port+"/api/test/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic MTIzOjEyMw==");
        Mockito.when(userService.authentication(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        assertEquals(connection.getResponseCode(), 405);
    }
}