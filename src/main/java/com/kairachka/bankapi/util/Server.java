package com.kairachka.bankapi.util;

import com.kairachka.bankapi.controller.*;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Server {
    public static void start() throws FileNotFoundException, SQLException {
        PropertiesManager propertiesManager = new PropertiesManager();
        UserController userController = new UserController();
        BillController billController = new BillController();
        CardController cardController = new CardController();
        ReplenishmentController replenishmentController = new ReplenishmentController();
        PartnerController partnerController = new PartnerController();
        OperationController operationController = new OperationController();
        Authenticator authenticator = new Authenticator();

        int serverPort = propertiesManager.getPort();
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        } catch (IOException e) {
            System.out.println("IO error");
        }
        assert server != null;

        server.createContext("/api/user/bills", billController).setAuthenticator(authenticator);
        server.createContext("/api/user/cards", cardController).setAuthenticator(authenticator);
        server.createContext("/api/user/balance", billController).setAuthenticator(authenticator);
        server.createContext("/api/user/replenishment", replenishmentController).setAuthenticator(authenticator);
        server.createContext("/api/user/partners", partnerController).setAuthenticator(authenticator);
        server.createContext("/api/user/operations", operationController).setAuthenticator(authenticator);

        server.createContext("/api/employee/bills", billController).setAuthenticator(authenticator);
        server.createContext("/api/employee/operations", operationController).setAuthenticator(authenticator);
        server.createContext("/api/employee/cards", cardController).setAuthenticator(authenticator);
        server.createContext("/api/employee/users", userController).setAuthenticator(authenticator);

        server.start();
        System.out.println("Server start");
    }
}

