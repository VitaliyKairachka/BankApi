package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Operation;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class OperationMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public Operation JsonToOperation(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Operation.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String OperationListToJson(List<Operation> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
