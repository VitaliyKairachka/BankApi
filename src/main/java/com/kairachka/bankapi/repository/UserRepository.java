package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;

import java.util.Optional;

public interface UserRepository {
    boolean addUser(User user);

    Optional<User> getUser(String login);
}
