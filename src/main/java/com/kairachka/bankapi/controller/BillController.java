package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.enums.RequestMethod;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.BillMapper;
import com.kairachka.bankapi.service.Impl.BillServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class BillController implements HttpHandler {
    private  BillServiceImpl billServiceImpl = new BillServiceImpl();
    private  UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final BillMapper billMapper = new BillMapper();

    public BillController() {
    }

    public BillController(BillServiceImpl billServiceImpl, UserServiceImpl userServiceImpl) {
        this.billServiceImpl = billServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (RequestMethod.GET.toString().equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null) {
                        try {
                            Bill bill = billServiceImpl.getBillByIdAndLogin(Long.parseLong(requestQuery.get("id")),
                                    exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200, billMapper.BillToJson(bill).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(billMapper.BillToJson(bill).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (NoAccessException e) {
                            System.out.println("No access");
                            exchange.sendResponseHeaders(403, -1);
                        } catch (BillNotFoundException e) {
                            System.out.println("Bill not found");
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else if (requestQuery.get("billId") != null) {
                        try {
                            double balance = billServiceImpl.getBalance(Long.parseLong(requestQuery.get("billId")),
                                    exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200,
                                    billMapper.balanceToJson(balance).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(billMapper.balanceToJson(balance).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (BillNotFoundException e) {
                            System.out.println("Bill not found");
                            exchange.sendResponseHeaders(404, -1);
                        } catch (NoAccessException e) {
                            System.out.println("No access");
                            exchange.sendResponseHeaders(403, -1);
                        }
                    } else if (requestQuery.isEmpty()) {
                        List<Bill> billList =
                                billServiceImpl.getAllBillsByUser
                                        (userServiceImpl.getUserIdByLogin(exchange.getPrincipal().getUsername()));
                        exchange.sendResponseHeaders(200, billMapper.BillListToJson(billList).getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(billMapper.BillListToJson(billList).getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if (RequestMethod.POST.toString().equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null) {
                        if (billServiceImpl.addBill(Long.parseLong(requestQuery.get("id")))) {
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


