package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.PartnerNotFoundException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.PartnerMapper;
import com.kairachka.bankapi.service.Impl.PartnerServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class PartnerController implements HttpHandler {
    private  PartnerServiceImpl partnerServiceImpl = new PartnerServiceImpl();
    private  UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final PartnerMapper partnerMapper = new PartnerMapper();

    public PartnerController() {
    }

    public PartnerController(PartnerServiceImpl partnerServiceImpl, UserServiceImpl userServiceImpl) {
        this.partnerServiceImpl = partnerServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.isEmpty()) {
                        try {
                            List<Partner> partnerList = partnerServiceImpl.getAllPartners();
                            exchange.sendResponseHeaders(200,
                                    partnerMapper.PartnerListToJson(partnerList).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(partnerMapper.PartnerListToJson(partnerList).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (PartnerNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
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
                        Partner partner = partnerMapper.JsonToPartner(exchange);
                        if (partnerServiceImpl.addPartner(partner)) {
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
