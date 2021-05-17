package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.sun.net.httpserver.HttpExchange;

public interface UserService {
    boolean addUser(HttpExchange exchange);

    long getUserIdByLogin(String login);

    boolean authentication(String login, String password);

    Role getRoleByLogin(String login);
}
