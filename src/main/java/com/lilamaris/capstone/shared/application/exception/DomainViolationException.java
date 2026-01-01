package com.lilamaris.capstone.shared.application.exception;

import lombok.Getter;

@Getter
public class DomainViolationException extends ApplicationException {
    private final String code;

    public DomainViolationException(String code, String message) {
        super("DOMAIN_VIOLATION", message);
        this.code = code;
    }
}
