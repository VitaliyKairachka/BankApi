package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    boolean addCard(Card card);

    List<Card> getAllCardsByBill(long billId);

    Optional<Card> getCardById(long cardId);

    List<Card> getAllCards();

    List<Card> getAllCardsByStatus(String status);

    boolean changeCardStatus(long cardId, String status);
}
