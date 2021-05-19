package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.entity.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class ReplenishmentMapper {
    ObjectMapper mapper = new ObjectMapper();
    public Replenishment JsonToReplenishment (HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Replenishment.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String ReplenishmentToJson (Replenishment replenishment)  {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(replenishment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String ReplenishmentListToJson(List<Replenishment> list){
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
