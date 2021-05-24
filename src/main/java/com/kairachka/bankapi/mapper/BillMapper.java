package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;

import java.util.List;

public class BillMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public String BillToJson(Bill bill) {
        try {
            return mapper.writeValueAsString(bill);
        } catch (JsonProcessingException e) {
            System.out.println("Json processing error");
            return null;
        }
    }

    public String BillListToJson(List<Bill> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            System.out.println("Json processing error");
            return null;
        }
    }

    public String balanceToJson(double balance) {
        try {
            return mapper.writeValueAsString(balance);
        } catch (JsonProcessingException e) {
            System.out.println("Json processing error");
            return null;
        }
    }
}
