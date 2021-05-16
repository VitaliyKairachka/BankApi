package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class UserMapper {
    ObjectMapper mapper = new ObjectMapper();

    public User JsonToUser (HttpExchange exchange) throws IOException {
        return mapper.readValue(exchange.getRequestBody(), User.class);
    }

    public String UserToJson (User user) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
    }
}
