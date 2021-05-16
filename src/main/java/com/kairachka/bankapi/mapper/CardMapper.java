package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Card;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class CardMapper {
    ObjectMapper mapper = new ObjectMapper();

    public Card JsonToCard (HttpExchange exchange) throws IOException {
        return mapper.readValue(exchange.getRequestBody(), Card.class);
    }

    public String CardToJson (Card card) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(card);
    }
}
