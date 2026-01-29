package com.lilamaris.capstone.auth.scenario.auth.application.exception;

public class ExpiredTokenException extends AuthException {
    public ExpiredTokenException(String message) {
        super("EXPIRED_TOKEN", message);
    }
}
