package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.mapper.BillMapper;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class BillController implements HttpHandler {
    BillService billService = new BillService();
    UserService userService = new UserService();
    BillMapper billMapper = new BillMapper();
    OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null) {
                        Bill bill = billService.getBillById(Long.parseLong(requestQuery.get("id")));
                        exchange.sendResponseHeaders(200, billMapper.BillToJson(bill).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(billMapper.BillToJson(bill).getBytes());
                    } else {
                        List<Bill> billList =
                                billService.getAllBillByUser
                                        (userService.getUserIdByLogin(exchange.getPrincipal().getUsername()));
                        exchange.sendResponseHeaders(200, billMapper.BillListToJson(billList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(billMapper.BillListToJson(billList).getBytes());
                    }
                    outputStream.flush();
                    exchange.close();
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (!requestQuery.get("id").isEmpty()) {
                        if (billService.addBill(Long.parseLong(requestQuery.get("id")))) {
                            exchange.sendResponseHeaders(201, -1);
                            exchange.close();
                        } else {
                            exchange.sendResponseHeaders(406, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(406, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else {
                try {
                    exchange.sendResponseHeaders(405, -1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            exchange.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

