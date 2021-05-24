package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.RequestMethod;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class UserController implements HttpHandler {
    private UserService userServiceImpl = new UserServiceImpl();
    private final UserMapper userMapper = new UserMapper();

    public UserController() {
    }

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (RequestMethod.POST.toString().equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        User user = userMapper.JsonToUser(exchange);
                        if (userServiceImpl.addUser(user)) {
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
            System.out.println("IO error");
        } catch (UserNotFoundException e) {
            System.out.println("User not found");
        }
    }
}

