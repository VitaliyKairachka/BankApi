package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class UserController implements HttpHandler {
    UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        if (userService.addUser(exchange)) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
