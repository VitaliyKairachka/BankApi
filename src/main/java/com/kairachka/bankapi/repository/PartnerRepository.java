package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    public boolean addPartner(Partner partner) {
        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PARTNERS (NAME, PARTNER_BILL) VALUES ( ?, ? )"
        )) {
            preparedStatement.setString(1, partner.getName());
            preparedStatement.setLong(2, partner.getPartnerBill());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Partner> getAllPartners() {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM PARTNERS"
             )) {
            resultSet = preparedStatement.executeQuery();
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
            e.printStackTrace();
            return null;
        }
    }
}
