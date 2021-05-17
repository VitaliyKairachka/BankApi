package com.kairachka.bankapi.repository.implement;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    @Override
    public boolean addUser(User user) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO USER(LOGIN, PASSWORD, ROLE) VALUES (?, ?, ?) ")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> getUser(String login) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM USER WHERE LOGIN = ?")) {
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            User user = new User(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    Role.valueOf(resultSet.getString(4)));
            return Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
