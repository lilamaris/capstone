package com.lilamaris.capstone.shared.domain.exception;

public class DomainIllegalArgumentException extends DomainException {
    public DomainIllegalArgumentException(String message) {
        super("DOMAIN_ILLEGAL_ARGUMENT", message);
    }
}
