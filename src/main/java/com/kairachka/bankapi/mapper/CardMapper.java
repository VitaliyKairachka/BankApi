package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Card;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class CardMapper {
    ObjectMapper mapper = new ObjectMapper();

    public Card JsonToCard (HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Card.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String CardToJson (Card card) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(card);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String CardListToJson(List<Card> list){
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
