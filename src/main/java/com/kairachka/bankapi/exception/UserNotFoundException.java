package com.kairachka.bankapi.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found exception");
    }
}
