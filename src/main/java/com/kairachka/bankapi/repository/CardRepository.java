package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    public boolean addCard(Card card) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO CARDS (EXPIRES, FIRST_NAME, LAST_NAME, BILL_ID) VALUES ( ?, ?, ?, ? )"
             )) {
            preparedStatement.setString(1, card.getExpires());
            preparedStatement.setString(2, card.getFirstName());
            preparedStatement.setString(3, card.getLastName());
            preparedStatement.setLong(4, card.getBillId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Card> getAllCardsByBill(long billId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM CARDS WHERE BILL_ID = ?"
             )) {
            preparedStatement.setLong(1, billId);
            resultSet = preparedStatement.executeQuery();
            List<Card> cardList = new ArrayList<>();
            while (resultSet.next()) {
                Card card = new Card(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getLong(6),
                        resultSet.getString(7)
                );
                cardList.add(card);
            }
            return cardList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Card> getCardById(long cardId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM CARDS WHERE ID = ?"
             )) {
            preparedStatement.setLong(1, cardId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Card card = new Card(
                    resultSet.getLong(1),
                    resultSet.getLong(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getLong(6),
                    resultSet.getString(7)
            );
            return Optional.of(card);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Card> getAllCards() {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM CARDS"
             )) {
            resultSet = preparedStatement.executeQuery();
            List<Card> cardList = new ArrayList<>();
            while (resultSet.next()) {
                Card card = new Card(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getLong(6),
                        resultSet.getString(7)
                );
                cardList.add(card);
            }
            return cardList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Card> getAllCardsByStatus(String status) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM CARDS WHERE STATUS = '?'"
             )) {
            preparedStatement.setString(1, status);
            resultSet = preparedStatement.executeQuery();
            List<Card> cardList = new ArrayList<>();
            while (resultSet.next()) {
                Card card = new Card(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getLong(6),
                        resultSet.getString(7)
                );
                cardList.add(card);
            }
            return cardList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean changeCardStatus (long cardId, String status) {
        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE CARDS SET STATUS = '?' WHERE ID = ?"
        )) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, cardId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
