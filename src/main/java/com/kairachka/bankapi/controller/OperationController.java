package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.OperationMapper;
import com.kairachka.bankapi.service.Impl.OperationServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class OperationController implements HttpHandler {
    private final OperationServiceImpl operationServiceImpl = new OperationServiceImpl();
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final OperationMapper operationMapper = new OperationMapper();
    private final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        try {
                            List<Operation> operationList =
                                    operationServiceImpl.getAllOperationsByBillId(
                                            Long.parseLong(requestQuery.get("billId")),
                                            exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200,
                                    operationMapper.OperationListToJson(operationList).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (OperationNotFoundException | BillNotFoundException | UserNotFoundException e) {
                            logger.info(e.getMessage());
                            exchange.sendResponseHeaders(404, -1);
                        } catch (NoAccessException e) {
                            logger.info(e.getMessage());
                            exchange.sendResponseHeaders(403, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        List<Operation> operationList = operationServiceImpl.getAllOperations();
                        exchange.sendResponseHeaders(200,
                                operationMapper.OperationListToJson(operationList).getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } else if (requestQuery.get("status") != null) {
                        List<Operation> operationList =
                                operationServiceImpl.getAllOperationsByStatus(requestQuery.get("status"));
                        exchange.sendResponseHeaders(200,
                                operationMapper.OperationListToJson(operationList).getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(operationMapper.OperationListToJson(operationList).getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        try {
                            if (operationServiceImpl.addOperation(exchange)) {
                                exchange.sendResponseHeaders(201, -1);
                            } else {
                                exchange.sendResponseHeaders(406, -1);
                            }
                        } catch (BillNotFoundException e) {
                            logger.info(e.getMessage());
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("PUT".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null && requestQuery.get("action") != null) {
                        try {
                            if (operationServiceImpl.changeStatusOperation(
                                    Long.parseLong(requestQuery.get("id")),
                                    requestQuery.get("action"))) {
                                exchange.sendResponseHeaders(200, -1);
                            } else {
                                exchange.sendResponseHeaders(406, -1);
                            }
                        } catch (OperationNotFoundException e) {
                            logger.info(e.getMessage());
                            exchange.sendResponseHeaders(404, -1);
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
