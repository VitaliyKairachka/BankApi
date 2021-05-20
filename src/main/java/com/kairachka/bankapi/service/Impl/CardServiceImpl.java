package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.CardStatus;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.repository.Impl.CardRepositoryImpl;
import com.kairachka.bankapi.service.CardService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CardServiceImpl implements CardService {
    private final CardRepositoryImpl cardRepositoryImpl = new CardRepositoryImpl();
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final BillServiceImpl billServiceImpl = new BillServiceImpl();

    public boolean addCard(String login, long billId) {
        try {
            User user = userServiceImpl.getUserByLogin(login);
            Bill bill = billServiceImpl.getBillById(billId);
            if (user.getId() == bill.getUserId()) {
                String date = DateTimeFormatter.ofPattern("MM/yy", Locale.ENGLISH)
                        .format(LocalDate.now().plusYears(2));
                Card card = new Card(
                        date,
                        user.getFirstName(),
                        user.getLastName(),
                        billId);
                return cardRepositoryImpl.addCard(card);
            } else {
                return false;
            }
        } catch (BillNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Card getCardById(long id, String login) {
        Optional<Card> card = cardRepositoryImpl.getCardById(id);
        if (card.isPresent()) {
            Bill bill = billServiceImpl.getBillById(card.get().getBillId());
            User user = userServiceImpl.getUserByLogin(login);
            if (user.getId() == bill.getUserId()) {
                return card.get();
            } else {
                throw new CardNotFoundException("Card not found exception");
            }
        } else {
            throw new CardNotFoundException();
        }
    }

    public List<Card> getAllCardsByBill(long id, String login) {
        try {
            User user = userServiceImpl.getUserByLogin(login);
            Bill bill = billServiceImpl.getBillById(id);
            if (user.getId() == bill.getUserId()) {
                return cardRepositoryImpl.getAllCardsByBill(id);
            } else {
                throw new BillNotFoundException();
            }
        } catch (BillNotFoundException e) {
            e.printStackTrace();
            throw new BillNotFoundException();
        }
    }

    public List<Card> getAllCards() {
        return cardRepositoryImpl.getAllCards();
    }

    public List<Card> getAllCardsByStatus(String status) {
        return cardRepositoryImpl.getAllCardsByStatus(status.toUpperCase());
    }

    public boolean changeCardStatus(long id, String status) {
        if (status.toUpperCase().equals(CardStatus.NOT_ACTIVE.toString())) {
            return cardRepositoryImpl.changeCardStatus(id, status.toUpperCase());
        } else if (status.toUpperCase().equals(CardStatus.ACTIVE.toString())) {
            return false;
        } else {
            return false;
        }
    }
}
