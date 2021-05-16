package com.kairachka.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UserController implements HttpHandler {
    UserMapper userMapper = new UserMapper();

    UserRepository userRepository = new UserRepository("jdbc:h2:file:C:/Users/Vitaliy/IdeaProjects/BankApi/src/main/resources/db");


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                userRepository.addUser(userMapper.JsonToUser(exchange));
                String text = "ok";
                exchange.sendResponseHeaders(200, text.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(text.getBytes());
                output.flush();
                exchange.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        } else if ("GET".equals(exchange.getRequestMethod())) {
            User user = new User("123", "123");
            String text = userMapper.UserToJson(user);
            exchange.sendResponseHeaders(200, text.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(text.getBytes());
            outputStream.flush();
            exchange.close();
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
