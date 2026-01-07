package com.lilamaris.capstone.scenario.auth.application.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final String code;

    protected AuthException(String code, String message) {
        super(message);
        this.code = code;
    }
}
