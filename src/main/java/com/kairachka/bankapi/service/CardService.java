package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;

import java.util.List;

/**
 *
 */
public interface CardService {
    /**
     *
     * @param login
     * @param billId
     * @return
     * @throws UserNotFoundException
     */
    boolean addCard(String login, long billId) throws UserNotFoundException;

    /**
     *
     * @param id
     * @param login
     * @return
     * @throws BillNotFoundException
     * @throws CardNotFoundException
     * @throws NoAccessException
     * @throws UserNotFoundException
     */
    Card getCardById(long id, String login)
            throws BillNotFoundException, CardNotFoundException, NoAccessException, UserNotFoundException;

    /**
     *
     * @param id
     * @param login
     * @return
     * @throws NoAccessException
     * @throws BillNotFoundException
     * @throws UserNotFoundException
     */
    List<Card> getAllCardsByBill(long id, String login)
            throws NoAccessException, BillNotFoundException, UserNotFoundException;

    /**
     *
     * @return
     */
    List<Card> getAllCards();

    /**
     *
     * @param status
     * @return
     */
    List<Card> getAllCardsByStatus(String status);

    /**
     *
     * @param id
     * @param status
     * @return
     */
    boolean changeCardStatus(long id, String status);
}
