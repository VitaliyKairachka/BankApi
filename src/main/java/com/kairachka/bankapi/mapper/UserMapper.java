package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class UserMapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    public User JsonToUser(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), User.class);
        } catch (IOException e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}
