package com.lilamaris.capstone.application.exception;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}
