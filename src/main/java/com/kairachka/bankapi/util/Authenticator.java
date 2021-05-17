package com.kairachka.bankapi.util;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.BasicAuthenticator;

import java.util.Optional;

public class Authenticator extends BasicAuthenticator {
    UserService userService = new UserService();
    PasswordEncryption passwordEncryption = new PasswordEncryption();

    public Authenticator(String realm) {
        super(realm);
    }



    @Override
    public boolean checkCredentials(String login, String password) {
        return userService.authentication(login, password);
    }
}
