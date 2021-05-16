package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository {
    private String url;

    public UserRepository (String url) {
        this.url = url;
    }

    public boolean addUser(User user) {
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO USER(LOGIN, PASSWORD) VALUES (?, ?) ")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
