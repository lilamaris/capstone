package com.lilamaris.capstone.auth.scenario.auth.application.exception;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message);
    }
}
