package com.kairachka.bankapi.util;

import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.BasicAuthenticator;

public class Authenticator extends BasicAuthenticator {
    UserService userService = new UserService();

    public Authenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String login, String password) {
        return userService.authentication(login, password);
    }
}
