package com.kairachka.bankapi.exception;

public class PartnerNotFoundException extends Exception {
    public PartnerNotFoundException() {
        super("Partner not found exception");
    }
}
