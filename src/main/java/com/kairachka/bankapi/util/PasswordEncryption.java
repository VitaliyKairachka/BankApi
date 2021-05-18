package com.kairachka.bankapi.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {
    public static String hashedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String candidate, String password) {
        System.out.println("hello pidoras na encodere");
        System.out.println(password);
        System.out.println(candidate);
        return BCrypt.checkpw(candidate, password);
    }

}
