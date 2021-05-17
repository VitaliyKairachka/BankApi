package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Card;

import java.util.List;

public interface CardRepository {
    boolean addCard(long billId);

    List<Card> getAllCardsByUser(long billId);
}
