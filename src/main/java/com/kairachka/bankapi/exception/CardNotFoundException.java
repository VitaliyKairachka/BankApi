package com.kairachka.bankapi.exception;

public class CardNotFoundException extends Exception {
    public CardNotFoundException() {
        super("Card not found exception");
    }
}
