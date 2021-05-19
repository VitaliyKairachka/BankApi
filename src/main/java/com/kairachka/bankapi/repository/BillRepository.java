package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.util.PropertiesManager;
import com.kairachka.bankapi.util.QuerySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    public boolean addBill(long userId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.ADD_BILL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Bill> getBillById(long billId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.GET_BILL_BY_ID)) {
            preparedStatement.setLong(1, billId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Bill bill = new Bill(
                    resultSet.getLong(1),
                    resultSet.getLong(2),
                    resultSet.getDouble(3),
                    resultSet.getLong(4)
            );
            return Optional.of(bill);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Bill> getAllBillsByUser(long userId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.GET_ALL_BILLS_BY_USER)) {
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();
            List<Bill> billList = new ArrayList<>();
            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getDouble(3),
                        resultSet.getLong(4)
                );
                billList.add(bill);
            }
            return billList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getBalanceBill(long billId) {
        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT BALANCE FROM BILLS WHERE ID = ?"
        )) {
            preparedStatement.setLong(1, billId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean plusBalance(long billId, double sum) {
        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE BILLS SET BALANCE = (SELECT BALANCE + ?) WHERE ID = ?"
        )) {
            preparedStatement.setDouble(1, sum);
            preparedStatement.setLong(2, billId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean minusBalance(long billId, double sum) {
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE BILLS SET BALANCE = (SELECT BALANCE - ?) WHERE ID = ?"
            )) {
            preparedStatement.setDouble(1, sum);
            preparedStatement.setLong(2, billId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
