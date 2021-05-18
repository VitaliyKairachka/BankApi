package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class UserController implements HttpHandler {
    UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                   if (userService.addUser(exchange)) {
                       exchange.sendResponseHeaders(201, -1);
                       exchange.close();
                   } else {
                       exchange.sendResponseHeaders(406, -1);
                   }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                exchange.sendResponseHeaders(405, -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        exchange.close();
    }
}
