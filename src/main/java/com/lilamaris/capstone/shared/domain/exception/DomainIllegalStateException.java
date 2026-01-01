package com.lilamaris.capstone.shared.domain.exception;

public class DomainIllegalStateException extends DomainException {
    public DomainIllegalStateException(String message) {
        super("DOMAIN_ILLEGAL_STATE", message);
    }
}
