package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Replenishment;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class ReplenishmentMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public Replenishment JsonToReplenishment(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Replenishment.class);
        } catch (IOException e) {
            System.out.println("Json processing error");
            return null;
        }
    }

    public String ReplenishmentListToJson(List<Replenishment> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            System.out.println("Json processing error");
            return null;
        }
    }
}
