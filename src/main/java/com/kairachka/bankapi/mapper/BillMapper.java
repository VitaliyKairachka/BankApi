package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BillMapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(BillMapper.class);

    public String BillToJson(Bill bill) {
        try {
            return mapper.writeValueAsString(bill);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            System.out.println(bill);
            return null;
        }
    }

    public String BillListToJson(List<Bill> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public String balanceToJson(double balance) {
        try {
            return mapper.writeValueAsString(balance);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}
