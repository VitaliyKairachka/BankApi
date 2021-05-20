package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.sun.net.httpserver.HttpExchange;

public interface UserService {
    boolean addUser(HttpExchange exchange);

    long getUserIdByLogin(String login) throws UserNotFoundException;

    boolean authentication(String login, String password);

    Role getRoleByLogin(String login) throws UserNotFoundException;

    User getUserByLogin(String login) throws UserNotFoundException;
}
