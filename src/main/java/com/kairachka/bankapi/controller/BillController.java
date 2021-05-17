package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.service.implement.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class BillController implements HttpHandler {
    UserServiceImpl userService = new UserServiceImpl();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {

                    exchange.sendResponseHeaders(201, "Created".length());
                    exchange.close();
                } else {
                    exchange.sendResponseHeaders(403, 0);
                }

//                exchange.sendResponseHeaders(200);
//                OutputStream output = exchange.getResponseBody();
//                output.write(text.getBytes());
//                output.flush();
//                exchange.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        exchange.close();
    }
}
