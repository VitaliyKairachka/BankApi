package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.sun.net.httpserver.HttpExchange;

/**
 *
 */
public interface UserService {
    /**
     *
     * @param exchange
     * @return
     */
    boolean addUser(HttpExchange exchange);

    /**
     *
     * @param login
     * @return
     * @throws UserNotFoundException
     */
    long getUserIdByLogin(String login) throws UserNotFoundException;

    /**
     *
     * @param login
     * @param password
     * @return
     */
    boolean authentication(String login, String password);

    /**
     *
     * @param login
     * @return
     * @throws UserNotFoundException
     */
    Role getRoleByLogin(String login) throws UserNotFoundException;

    /**
     *
     * @param login
     * @return
     * @throws UserNotFoundException
     */
    User getUserByLogin(String login) throws UserNotFoundException;
}
