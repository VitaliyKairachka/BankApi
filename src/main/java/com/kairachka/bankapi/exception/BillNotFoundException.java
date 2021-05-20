package com.kairachka.bankapi.exception;

public class BillNotFoundException extends Exception {
    public BillNotFoundException() {
        super("Bill not found exception");
    }
}
