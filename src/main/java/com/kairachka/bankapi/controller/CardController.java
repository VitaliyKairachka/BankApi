package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.mapper.CardMapper;
import com.kairachka.bankapi.service.CardService;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CardController implements HttpHandler {
    UserService userService = new UserService();
    CardService cardService = new CardService();
    CardMapper cardMapper = new CardMapper();
    OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        List<Card> cardList = cardService.getAllCardsByBill(Long.parseLong(requestQuery.get("billId")));
                        exchange.sendResponseHeaders(200, cardMapper.CardListToJson(cardList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardListToJson(cardList).getBytes());
                    } else if (requestQuery.get("id") != null) {
                        Card card = cardService.getCardById(Long.parseLong(requestQuery.get("id")));
                        exchange.sendResponseHeaders(200, cardMapper.CardToJson(card).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardToJson(card).getBytes());
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                    outputStream.flush();
                    exchange.close();
                } else if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("status") != null) {
                        List<Card> cardList = cardService.getAllCardsByStatus(requestQuery.get("status"));
                        exchange.sendResponseHeaders(200, cardMapper.CardListToJson(cardList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardListToJson(cardList).getBytes().length);
                    } else if (requestQuery.isEmpty()) {
                        List<Card> cardList = cardService.getAllCards();
                        exchange.sendResponseHeaders(200, cardMapper.CardListToJson(cardList).getBytes().length);
                        outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardListToJson(cardList).getBytes());
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                    outputStream.flush();
                } else {
                    exchange.sendResponseHeaders(403, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                if (userService.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        if (cardService.addCard(exchange.getPrincipal().getUsername(),
                                Long.parseLong(requestQuery.get("billId")))) {
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
                        if (cardService.changeCardStatus(Long.parseLong(requestQuery.get("id")),
                                requestQuery.get("action"))) {
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
