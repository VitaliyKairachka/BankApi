package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.mapper.OperationMapper;
import com.kairachka.bankapi.service.OperationService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class OperationController implements HttpHandler {
    OperationService operationService = new OperationService();
    UserService userService = new UserService();
    OperationMapper operationMapper = new OperationMapper();
    OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        try {
                            List<Operation> operationList =
                                    operationService.getAllOperationsByBillId(Long.parseLong(requestQuery.get("billId")), exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200, operationMapper.OperationListToJson(operationList).getBytes().length);
                            outputStream = exchange.getResponseBody();
                            outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                            outputStream.flush();
                        } catch (OperationNotFoundException | BillNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        List<Operation> operationList = operationService.getAllOperations();
                        exchange.sendResponseHeaders(200, operationMapper.OperationListToJson(operationList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                        outputStream.flush();
                    } else if (requestQuery.get("status") != null) {
                        List<Operation> operationList = operationService.getAllOperationsByStatus(requestQuery.get("status"));
                        exchange.sendResponseHeaders(200, operationMapper.OperationListToJson(operationList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                        outputStream.flush();
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
                        if (operationService.addOperation(exchange)) {
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
            } else if ("PUT".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null && requestQuery.get("action") != null) {
                        if (operationService.changeStatusOperation(
                                Long.parseLong(requestQuery.get("id")),
                                requestQuery.get("action"))) {
                            exchange.sendResponseHeaders(200, -1);
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
