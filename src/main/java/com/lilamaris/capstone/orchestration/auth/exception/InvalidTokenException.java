package com.lilamaris.capstone.orchestration.auth.exception;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message);
    }
}
