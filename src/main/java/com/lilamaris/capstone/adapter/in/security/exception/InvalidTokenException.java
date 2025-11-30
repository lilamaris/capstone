package com.lilamaris.capstone.adapter.in.security.exception;

public class InvalidTokenException extends SecurityException {
    public InvalidTokenException(String message) {
        super("SECURITY_INVALID_TOKEN", message);
    }
}
