package com.lilamaris.capstone.application.exception;

public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super("RESOURCE_CONFLICT", message);
    }
}
