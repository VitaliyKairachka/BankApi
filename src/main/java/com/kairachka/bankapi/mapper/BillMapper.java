package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class BillMapper {
    ObjectMapper mapper = new ObjectMapper();

    public Bill JsonToBill (HttpExchange exchange) throws IOException {
        return mapper.readValue(exchange.getRequestBody(), Bill.class);
    }

    public String BillToJson (Bill bill) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bill);
    }
}
