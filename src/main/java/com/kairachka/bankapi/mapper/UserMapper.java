package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class UserMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public User JsonToUser(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
