package com.kairachka.bankapi.util;

import com.kairachka.bankapi.service.Impl.UserServiceImpl;
import com.sun.net.httpserver.BasicAuthenticator;

public class Authenticator extends BasicAuthenticator {
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();

    public Authenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String login, String password) {
        return userServiceImpl.authentication(login, password);
    }
}
