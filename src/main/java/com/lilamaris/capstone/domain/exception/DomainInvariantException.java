package com.lilamaris.capstone.domain.exception;

public class DomainInvariantException extends DomainException {
    public DomainInvariantException(String message) {
        super("DOMAIN_INVARIANT_VIOLATION", message);
    }
}
