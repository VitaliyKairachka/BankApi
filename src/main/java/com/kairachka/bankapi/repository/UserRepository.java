package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.User;

import java.util.Optional;

public interface UserRepository {
    boolean addUser(User user);

    Optional<User> getUserByLogin(String login);
}
