package com.kairachka.bankapi.util;

import com.kairachka.bankapi.controller.*;
import com.sun.net.httpserver.HttpServer;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
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
        Logger logger = LoggerFactory.getLogger(Server.class);

        int serverPort = propertiesManager.getPort();
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        } catch (IOException e) {
            logger.info(e.getMessage());
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

        Connection connection = DriverManager.getConnection(propertiesManager.getUrl());
        String createTable = "src/main/resources/SQLScripts/CreateTables.sql";
        String createAdminUser = "src/main/resources/SQLScripts/CreateAdminUser";
        RunScript.execute(connection, new FileReader(createTable));
        RunScript.execute(connection, new FileReader(createAdminUser));

        server.start();
        logger.info("Server start");
    }
}

