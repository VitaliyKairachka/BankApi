package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.CardStatus;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.CardNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.CardRepository;
import com.kairachka.bankapi.repository.Impl.CardRepositoryImpl;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.CardService;
import com.kairachka.bankapi.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository = new CardRepositoryImpl();
    private final UserService userService = new UserServiceImpl();
    private final BillService billService = new BillServiceImpl();

    @Override
    public boolean addCard(String login, long billId) throws UserNotFoundException {
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
                return cardRepository.addCard(card);
            } else {
                return false;
            }
        } catch (BillNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Card getCardById(long id, String login)
            throws BillNotFoundException, CardNotFoundException, NoAccessException, UserNotFoundException {
        Optional<Card> card = cardRepository.getCardById(id);
        if (card.isPresent()) {
            Bill bill = billService.getBillById(card.get().getBillId());
            User user = userService.getUserByLogin(login);
            if (user.getId() == bill.getUserId()) {
                return card.get();
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new CardNotFoundException();
        }
    }

    @Override
    public List<Card> getAllCardsByBill(long id, String login) throws NoAccessException, BillNotFoundException, UserNotFoundException {
        try {
            User user = userService.getUserByLogin(login);
            Bill bill = billService.getBillById(id);
            if (user.getId() == bill.getUserId()) {
                return cardRepository.getAllCardsByBill(id);
            } else {
                throw new NoAccessException();
            }
        } catch (BillNotFoundException e) {
            throw new BillNotFoundException();
        }
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.getAllCards();
    }

    @Override
    public List<Card> getAllCardsByStatus(String status) {
        return cardRepository.getAllCardsByStatus(status.toUpperCase());
    }

    @Override
    public boolean changeCardStatus(long id, String status) {
        if (status.toUpperCase().equals(CardStatus.NOT_ACTIVE.toString())) {
            return cardRepository.changeCardStatus(id, status.toUpperCase());
        } else if (status.toUpperCase().equals(CardStatus.ACTIVE.toString())) {
            return false;
        } else {
            return false;
        }
    }
}
