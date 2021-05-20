package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.PasswordEncryption;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
    private final UserMapper userMapper = new UserMapper();

    public boolean addUser(HttpExchange exchange) {
        User user = userMapper.JsonToUser(exchange);
        user.setPassword(PasswordEncryption.hashedPassword(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepositoryImpl.addUser(user);
    }

    public long getUserIdByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepositoryImpl.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get().getId();
        } else {
            throw new UserNotFoundException();
        }
    }

    public boolean authentication(String login, String password) {
        Optional<User> user = userRepositoryImpl.getUserByLogin(login);
        return user.filter(value -> PasswordEncryption.checkPassword(password, value.getPassword())).isPresent();
    }

    public Role getRoleByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepositoryImpl.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get().getRole();
        } else {
            throw new UserNotFoundException();
        }
    }

    public User getUserByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepositoryImpl.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}
