package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;

import java.util.List;

public interface CardService {
    boolean addCard(String login, long billId) throws UserNotFoundException;

    Card getCardById(long id, String login)
            throws BillNotFoundException, CardNotFoundException, NoAccessException, UserNotFoundException;

    List<Card> getAllCardsByBill(long id, String login)
            throws NoAccessException, BillNotFoundException, UserNotFoundException;

    List<Card> getAllCards();

    List<Card> getAllCardsByStatus(String status);

    boolean changeCardStatus(long id, String status);
}
