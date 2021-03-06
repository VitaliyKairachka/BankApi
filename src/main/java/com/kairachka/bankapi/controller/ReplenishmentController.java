package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.enums.RequestMethod;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.service.Impl.ReplenishmentServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ReplenishmentController implements HttpHandler {
    private ReplenishmentServiceImpl replenishmentServiceImpl = new ReplenishmentServiceImpl();
    private UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();

    public ReplenishmentController() {
    }

    public ReplenishmentController(ReplenishmentServiceImpl replenishmentServiceImpl, UserServiceImpl userServiceImpl) {
        this.replenishmentServiceImpl = replenishmentServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (RequestMethod.GET.toString().equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        try {
                            List<Replenishment> replenishmentList =
                                    replenishmentServiceImpl.
                                            getAllReplenishmentByBill(Long.parseLong(requestQuery.get("billId")),
                                                    exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200,
                                    replenishmentMapper.ReplenishmentListToJson(replenishmentList).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(
                                    replenishmentMapper.ReplenishmentListToJson(replenishmentList).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (BillNotFoundException | UserNotFoundException e) {
                            System.out.println("Bill or user not found");
                            exchange.sendResponseHeaders(404, -1);
                        } catch (NoAccessException e) {
                            System.out.println("No access");
                            exchange.sendResponseHeaders(403, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if (RequestMethod.POST.toString().equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        Replenishment replenishment = replenishmentMapper.JsonToReplenishment(exchange);
                        if (replenishmentServiceImpl.addReplenishment(replenishment)) {
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
        } catch (IOException e) {
            System.out.println("IO error");
        } catch (UserNotFoundException e) {
            System.out.println("User not found");
        }
    }
}
