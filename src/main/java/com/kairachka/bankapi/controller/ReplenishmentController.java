package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.service.ReplenishmentService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ReplenishmentController implements HttpHandler {
    ReplenishmentService replenishmentService = new ReplenishmentService();
    UserService userService = new UserService();
    ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();
    OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        List<Replenishment> replenishmentList =
                                replenishmentService.
                                        getAllReplenishmentByBill(Long.parseLong(requestQuery.get("billId")));
                        exchange.sendResponseHeaders(200,
                                replenishmentMapper.ReplenishmentListToJson(replenishmentList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(replenishmentMapper.ReplenishmentListToJson(replenishmentList).getBytes());
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        if (replenishmentService.addReplenishment(exchange)) {
                            exchange.sendResponseHeaders(201, -1);
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
