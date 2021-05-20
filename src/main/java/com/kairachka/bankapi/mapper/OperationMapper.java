package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Operation;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class OperationMapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OperationMapper.class);

    public Operation JsonToOperation(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Operation.class);
        } catch (IOException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public String OperationListToJson(List<Operation> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}
