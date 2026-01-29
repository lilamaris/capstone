package com.lilamaris.capstone.auth.scenario.auth.application.exception;

public class AuthenticationException extends AuthException {
    public AuthenticationException(String message) {
        super("AUTHENTICATION_FAILED", message);
    }
}
