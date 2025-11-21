package com.lilamaris.capstone.domain.exception;

public class DomainIllegalStateException extends DomainException {
    public DomainIllegalStateException(String message) {
        super("DOMAIN_ILLEGAL_STATE", message);
    }
}
