package com.lilamaris.capstone.identity.auth.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationProcessingException extends AuthenticationException {
    public AuthenticationProcessingException(String message) {
        super(message);
    }
}
