package com.lilamaris.capstone.domain.exception;

public class DomainIllegalArgumentException extends DomainException {
    public DomainIllegalArgumentException(String message) {
        super("DOMAIN_ILLEGAL_ARGUMENT", message);
    }
}
