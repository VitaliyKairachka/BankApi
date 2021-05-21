package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.repository.UserRepository;
import com.kairachka.bankapi.service.UserService;
import com.kairachka.bankapi.util.PasswordEncryption;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository = new UserRepositoryImpl();
    private final UserMapper userMapper = new UserMapper();

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean addUser(HttpExchange exchange) {
        User user = userMapper.JsonToUser(exchange);
        user.setPassword(PasswordEncryption.hashedPassword(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.addUser(user);
    }

    @Override
    public long getUserIdByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get().getId();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean authentication(String login, String password) {
        Optional<User> user = userRepository.getUserByLogin(login);
        return user.filter(value -> PasswordEncryption.checkPassword(password, value.getPassword())).isPresent();
    }

    @Override
    public Role getRoleByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get().getRole();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User getUserByLogin(String login) throws UserNotFoundException {
        Optional<User> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}
