package com.kairachka.bankapi.repository.Impl;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.repository.PartnerRepository;
import com.kairachka.bankapi.util.PropertiesManager;
import com.kairachka.bankapi.util.QuerySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerRepositoryImpl implements PartnerRepository {
    private final PropertiesManager propertiesManager = new PropertiesManager();
    private String url = propertiesManager.getUrl();

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean addPartner(Partner partner) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.ADD_PARTNER)) {
            preparedStatement.setString(1, partner.getName());
            preparedStatement.setLong(2, partner.getPartnerBill());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL error");
            return false;
        }
    }

    @Override
    public List<Partner> getAllPartners() {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(QuerySQL.GET_ALL_PARTNERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Partner> partnerList = new ArrayList<>();
            while (resultSet.next()) {
                Partner partner = new Partner(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getLong(3)
                );
                partnerList.add(partner);
            }
            return partnerList;
        } catch (SQLException e) {
            System.out.println("SQL error");
            return null;
        }
    }
}
