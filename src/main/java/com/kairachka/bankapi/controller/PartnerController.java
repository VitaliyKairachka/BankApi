package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.mapper.PartnerMapper;
import com.kairachka.bankapi.service.PartnerService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class PartnerController implements HttpHandler {
    PartnerService partnerService = new PartnerService();
    UserService userService = new UserService();
    PartnerMapper partnerMapper = new PartnerMapper();
    OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        List<Partner> partnerList = partnerService.getAllPartners();
                        exchange.sendResponseHeaders(200,
                                partnerMapper.PartnerListToJson(partnerList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(partnerMapper.PartnerListToJson(partnerList).getBytes());
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
                        if (partnerService.addPartner(exchange)) {
                            exchange.sendResponseHeaders(201, -1);
                        } else {
                            exchange.sendResponseHeaders(201, -1);
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
