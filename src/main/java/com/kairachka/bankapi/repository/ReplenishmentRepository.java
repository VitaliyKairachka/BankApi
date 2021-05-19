package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentRepository{
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    public boolean addReplenishment(Replenishment replenishment) {
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO REPLENISHMENTS (SUM, BILL_ID) VALUES ( ?, ? )"
            )) {
          preparedStatement.setDouble(1, replenishment.getSum());
          preparedStatement.setLong(2, replenishment.getBillId());
          preparedStatement.execute();
          return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Replenishment> getAllReplenishmentByBill(long billId) {
        try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM REPLENISHMENTS WHERE BILL_ID = ?"
        )) {
            preparedStatement.setLong(1, billId);
            resultSet = preparedStatement.executeQuery();
            List<Replenishment> replenishmentList = new ArrayList<>();
            while (resultSet.next()) {
                Replenishment replenishment = new Replenishment(
                        resultSet.getLong(1),
                        resultSet.getDouble(2),
                        resultSet.getLong(3)
                );
                replenishmentList.add(replenishment);
            }
            return replenishmentList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
