package com.kairachka.bankapi.repository.implement;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.List;

public class BillRepositoryImpl implements BillRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    @Override
    public boolean addBill(long userId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO BILL(USER_ID) VALUES (?)")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Bill> getAllBillsByUser(long userId) {
        try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM BILL WHERE ID = ?")) {
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
