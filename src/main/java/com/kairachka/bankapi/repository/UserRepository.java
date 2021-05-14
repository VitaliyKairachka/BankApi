package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private String url;

    public UserRepository (String url) {
        this.url = url;
    }

    public String addUser(User user) {
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
