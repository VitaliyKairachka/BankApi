package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class UserController implements HttpHandler {
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        if (userServiceImpl.addUser(exchange)) {
                            exchange.sendResponseHeaders(201, -1);
                            exchange.close();
                        } else {
                            exchange.sendResponseHeaders(406, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (UserNotFoundException e) {
            logger.info(e.getMessage());
        }
    }
}
