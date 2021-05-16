package com.kairachka.bankapi.service;

import com.kairachka.bankapi.controller.UserController;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {
    public static void start() {
        UserController userController = new UserController();
        Authenticator authenticator = new Authenticator("realm");
        int serverPort = 8080;
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert server != null;
        server.createContext("/api/user", userController).setAuthenticator(authenticator);
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}

