package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.mapper.BillMapper;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null) {
                        try {
                            Bill bill = billService.getBillById(Long.parseLong(requestQuery.get("id")));
                            exchange.sendResponseHeaders(200, billMapper.BillToJson(bill).getBytes().length);
                            outputStream = exchange.getResponseBody();
                            outputStream.write(billMapper.BillToJson(bill).getBytes());
                            outputStream.flush();
                        } catch (BillNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else if (requestQuery.get("billId") != null) {
                        try {
                            double balance = billService.getBalance(Long.parseLong(requestQuery.get("billId")), exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200, billMapper.balanceToJson(balance).getBytes().length);
                            outputStream = exchange.getResponseBody();
                            outputStream.write(billMapper.balanceToJson(balance).getBytes());
                            outputStream.flush();
                        } catch (BillNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else if (requestQuery.isEmpty()) {
                        List<Bill> billList =
                                billService.getAllBillsByUser
                                        (userService.getUserIdByLogin(exchange.getPrincipal().getUsername()));
                        exchange.sendResponseHeaders(200, billMapper.BillListToJson(billList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(billMapper.BillListToJson(billList).getBytes());
                        outputStream.flush();
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null) {
                        if (billService.addBill(Long.parseLong(requestQuery.get("id")))) {
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

