package com.kairachka.bankapi.service;

import com.sun.net.httpserver.BasicAuthenticator;

public class Authenticator extends BasicAuthenticator {

    public Authenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String login, String password) {
        return login.equals("admin") && password.equals("admin");
    }
}
