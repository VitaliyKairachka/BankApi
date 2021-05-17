package com.kairachka.bankapi.util;

import com.kairachka.bankapi.service.implement.UserServiceImpl;
import com.sun.net.httpserver.BasicAuthenticator;

public class Authenticator extends BasicAuthenticator {
    UserServiceImpl userService = new UserServiceImpl();

    public Authenticator(String realm) {
        super(realm);
    }



    @Override
    public boolean checkCredentials(String login, String password) {
        return userService.authentication(login, password);
    }
}
