package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Operation;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class OperationMapper {
    ObjectMapper mapper = new ObjectMapper();

    public Operation JsonToOperation(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Operation.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String OperationToJson(Operation operation) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(operation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
