package com.kairachka.bankapi.exception;

public class OperationNotFoundException extends RuntimeException{
    public OperationNotFoundException(String message) {
        super(message);
    }
}
