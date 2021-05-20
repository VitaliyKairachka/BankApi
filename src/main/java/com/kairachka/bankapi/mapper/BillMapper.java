package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class BillMapper {
    ObjectMapper mapper = new ObjectMapper();

    public Bill JsonToBill(HttpExchange exchange) {
        try {
            return mapper.readValue(exchange.getRequestBody(), Bill.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String BillToJson(Bill bill) {
        try {
            return mapper.writeValueAsString(bill);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String BillListToJson(List<Bill> list){
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String balanceToJson(double balance) {
        try {
            return mapper.writeValueAsString(balance);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
