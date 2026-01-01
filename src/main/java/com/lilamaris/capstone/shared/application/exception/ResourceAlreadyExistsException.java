package com.lilamaris.capstone.shared.application.exception;

public class ResourceAlreadyExistsException extends ApplicationException {
    public ResourceAlreadyExistsException(String message) {
        super("RESOURCE_EXISTS", message);
    }
}
