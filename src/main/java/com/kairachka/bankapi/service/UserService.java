package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.util.PasswordEncryption;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

public class UserService {
    UserRepository userRepository = new UserRepository();
    UserMapper userMapper = new UserMapper();
    PasswordEncryption passwordEncryption = new PasswordEncryption();

    public boolean addUser(HttpExchange exchange) {
        User user = userMapper.JsonToUser(exchange);
        user.setPassword(passwordEncryption.hashedPassword(user.getPassword()));
        return userRepository.addUser(user);
    }

    public long getUserIdByLogin(String login) {
        Optional<User> user = userRepository.getUser(login);
        return user.map(User::getId).orElse(0L);
    }

    public boolean authentication(String login, String password) {
        Optional<User> user = userRepository.getUser(login);
        return user.filter(value -> passwordEncryption.checkPassword(password, value.getPassword())).isPresent();
//        return user.map(value -> value.getPassword().equals(password)).orElse(false);
    }

    public Role getRoleByLogin(String login) {
        Optional<User> user = userRepository.getUser(login);
        if (user.isPresent()) return user.get().getRole();
        else throw new UserNotFoundException();
    }
}
