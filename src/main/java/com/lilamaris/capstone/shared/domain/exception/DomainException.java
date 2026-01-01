package com.lilamaris.capstone.shared.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final String code;

    protected DomainException(String code, String message) {
        super(message);
        this.code = code;
    }
}
