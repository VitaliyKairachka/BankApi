package com.kairachka.bankapi.exception;

public class OperationNotFoundException extends Exception {
    public OperationNotFoundException() {
        super("Operation not found");
    }
}
