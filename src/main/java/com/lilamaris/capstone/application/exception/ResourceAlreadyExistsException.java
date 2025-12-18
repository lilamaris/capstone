package com.lilamaris.capstone.application.exception;

public class ResourceAlreadyExistsException extends ApplicationException {
    public ResourceAlreadyExistsException(String message) {
        super("RESOURCE_EXISTS", message);
    }
}
