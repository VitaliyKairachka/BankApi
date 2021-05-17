package com.kairachka.bankapi.util;

import com.kairachka.bankapi.controller.UserController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void start() {
        PropertiesManager propertiesManager = new PropertiesManager();
        UserController userController = new UserController();
        Authenticator authenticator = new Authenticator(propertiesManager.getRealm());
        int serverPort = propertiesManager.getPort();
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

