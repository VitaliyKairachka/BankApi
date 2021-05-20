package com.kairachka.bankapi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CardMapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(CardMapper.class);

    public String CardToJson(Card card) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(card);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public String CardListToJson(List<Card> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}
