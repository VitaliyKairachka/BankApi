package com.kairachka.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class UserController implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String respText = "Hello user!";
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
        } else {
            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
        }
        exchange.close();
    }


}
