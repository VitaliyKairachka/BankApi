package com.kairachka.bankapi.repository.Impl;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.repository.ReplenishmentRepository;
import com.kairachka.bankapi.util.PropertiesManager;
import com.kairachka.bankapi.util.QuerySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentRepositoryImpl implements ReplenishmentRepository {
    private final PropertiesManager propertiesManager = new PropertiesManager();
    private String url = propertiesManager.getUrl();

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean addReplenishment(Replenishment replenishment) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.ADD_REPLENISHMENT)) {
            preparedStatement.setDouble(1, replenishment.getSum());
            preparedStatement.setLong(2, replenishment.getBillId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL error");
            return false;
        }
    }

    @Override
    public List<Replenishment> getAllReplenishmentByBill(long billId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement =
                     connection.prepareStatement(QuerySQL.GET_ALL_REPLENISHMENT_BY_BILL)) {
            preparedStatement.setLong(1, billId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
            System.out.println("SQL error");
            return null;
        }
    }
}
