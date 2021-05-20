package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Card;

import java.util.List;

public interface CardService {
    boolean addCard(String login, long billId);

    Card getCardById(long id, String login);

    List<Card> getAllCardsByBill(long id, String login);

    List<Card> getAllCards();

    List<Card> getAllCardsByStatus(String status);

    boolean changeCardStatus(long id, String status);
}
