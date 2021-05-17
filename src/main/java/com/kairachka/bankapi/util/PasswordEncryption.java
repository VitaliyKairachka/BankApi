package com.kairachka.bankapi.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {
    public String hashedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String candidate, String password) {
        return BCrypt.checkpw(candidate, password);
    }

}
