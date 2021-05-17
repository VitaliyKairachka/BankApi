package com.kairachka.bankapi.service.implement;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.implement.UserRepositoryImpl;
import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    UserRepositoryImpl userRepository = new UserRepositoryImpl();
    UserMapper userMapper = new UserMapper();


    @Override
    public boolean addUser(HttpExchange exchange) {
        return userRepository.addUser(userMapper.JsonToUser(exchange));
    }

    @Override
    public long getUserIdByLogin(String login) {
        Optional<User> user = userRepository.getUser(login);
        return user.map(User::getId).orElse(0L);
    }

    @Override
    public boolean authentication(String login, String password) {
        Optional<User> user = userRepository.getUser(login);
        return user.map(value -> value.getPassword().equals(password)).orElse(false);
    }

    @Override
    public Role getRoleByLogin(String login) {
        Optional<User> user = userRepository.getUser(login);
        if (user.isPresent()) return user.get().getRole();
        else throw new UserNotFoundException();
    }


}
