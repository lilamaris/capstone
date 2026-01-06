package com.lilamaris.capstone.orchestration.auth.exception;

public class ExpiredTokenException extends AuthException {
    public ExpiredTokenException(String message) {
        super("EXPIRED_TOKEN", message);
    }
}
