package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.repository.CardRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CardService {
    CardRepository cardRepository = new CardRepository();
    UserService userService = new UserService();
    BillService billService = new BillService();

    public boolean addCard(String login, long billId) {
        try {
            User user = userService.getUserByLogin(login);
            Bill bill = billService.getBillById(billId);
            if (user.getId() == bill.getUserId()) {
                String date = DateTimeFormatter.ofPattern("MM/yy", Locale.ENGLISH)
                        .format(LocalDate.now().plusYears(2));
                Card card = new Card(
                        date,
                        user.getFirstName(),
                        user.getLastName(),
                        billId);
                cardRepository.addCard(card);
                return true;
            } else {
                return false;
            }
        } catch (BillNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Card getCardById(long id, String login) {
        Optional<Card> card = cardRepository.getCardById(id);
        if (card.isPresent()) {
            Bill bill = billService.getBillById(card.get().getBillId());
            User user = userService.getUserByLogin(login);
            if (user.getId() == bill.getUserId()) {
                return card.get();
            } else {
                throw new CardNotFoundException("Card not found exception");
            }
        } else {
            throw new CardNotFoundException("Card not found exception");
        }
    }

    public List<Card> getAllCardsByBill(long id, String login) {
        try {
            User user = userService.getUserByLogin(login);
            Bill bill = billService.getBillById(id);
            if (user.getId() == bill.getUserId()) {
                return cardRepository.getAllCardsByBill(id);
            } else {
                throw new BillNotFoundException("Bill not found exception");
            }
        } catch (BillNotFoundException e) {
            e.printStackTrace();
            throw new BillNotFoundException("Bill not found exception");
        }
    }

    public List<Card> getAllCards() {
        return cardRepository.getAllCards();
    }

    public List<Card> getAllCardsByStatus(String status) {
        return cardRepository.getAllCardsByStatus(status.toUpperCase());
    }

    public boolean changeCardStatus(long id, String status) {
        return cardRepository.changeCardStatus(id, status.toUpperCase());
    }
}
