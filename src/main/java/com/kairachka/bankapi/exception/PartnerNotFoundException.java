package com.kairachka.bankapi.exception;

public class PartnerNotFoundException extends RuntimeException{
    public PartnerNotFoundException(String message) {
        super(message);
    }
}
