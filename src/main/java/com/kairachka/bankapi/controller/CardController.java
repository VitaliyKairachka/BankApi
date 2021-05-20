package com.kairachka.bankapi.controller;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.mapper.CardMapper;
import com.kairachka.bankapi.service.Impl.CardServiceImpl;
import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.kairachka.bankapi.util.QueryParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CardController implements HttpHandler {
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final CardServiceImpl cardServiceImpl = new CardServiceImpl();
    private final CardMapper cardMapper = new CardMapper();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.USER)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("billId") != null) {
                        try {
                            List<Card> cardList = cardServiceImpl.getAllCardsByBill(
                                    Long.parseLong(requestQuery.get("billId")),
                                    exchange.getPrincipal().getUsername());
                            if (cardList.get(0) != null) {
                                exchange.sendResponseHeaders(200,
                                        cardMapper.CardListToJson(cardList).getBytes().length);
                                OutputStream outputStream = exchange.getResponseBody();
                                outputStream.write(cardMapper.CardListToJson(cardList).getBytes());
                                outputStream.flush();
                                outputStream.close();
                            } else {
                                exchange.sendResponseHeaders(404, -1);
                            }
                        } catch (BillNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else if (requestQuery.get("id") != null) {
                        try {
                            Card card = cardServiceImpl.getCardById(Long.parseLong(requestQuery.get("id")),
                                    exchange.getPrincipal().getUsername());
                            exchange.sendResponseHeaders(200, cardMapper.CardToJson(card).getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(cardMapper.CardToJson(card).getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (CardNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                    exchange.close();
                } else if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("status") != null) {
                        List<Card> cardList = cardServiceImpl.getAllCardsByStatus(requestQuery.get("status"));
                        exchange.sendResponseHeaders(200, cardMapper.CardListToJson(cardList).getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardListToJson(cardList).getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } else if (requestQuery.isEmpty()) {
                        List<Card> cardList = cardServiceImpl.getAllCards();
                        exchange.sendResponseHeaders(200, cardMapper.CardListToJson(cardList).getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(cardMapper.CardListToJson(cardList).getBytes());
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
                    if (requestQuery.get("billId") != null) {
                        if (cardServiceImpl.addCard(exchange.getPrincipal().getUsername(),
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
                if (userServiceImpl.getRoleByLogin(exchange.getPrincipal().getUsername()).equals(Role.EMPLOYEE)) {
                    Map<String, String> requestQuery = QueryParser.queryToMap(exchange.getRequestURI().getRawQuery());
                    if (requestQuery.get("id") != null && requestQuery.get("action") != null) {
                        if (cardServiceImpl.changeCardStatus(Long.parseLong(requestQuery.get("id")),
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
