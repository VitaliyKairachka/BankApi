package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.entity.User;
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

    public boolean addCard(String login ,long billId) {
        User user = userService.getUserByLogin(login);
        String date = DateTimeFormatter.ofPattern("MM/yy", Locale.ENGLISH)
                .format(LocalDate.now().plusYears(2));
        Card card = new Card(
                date,
                user.getFirstName(),
                user.getLastName(),
                billId);
        return cardRepository.addCard(card);
    }

    public Card getCardById (long id) {
        Optional<Card> card = cardRepository.getCardById(id);
        if(card.isPresent()) {
            return card.get();
        } else {
            throw new CardNotFoundException("Card not found exception");
        }
    }

    public List<Card> getAllCardsByBill (long id) {
        return cardRepository.getAllCardsByBill(id);
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
