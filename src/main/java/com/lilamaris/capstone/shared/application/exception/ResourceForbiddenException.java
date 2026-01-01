package com.lilamaris.capstone.shared.application.exception;

public class ResourceForbiddenException extends ApplicationException {
    public ResourceForbiddenException() {
        super("FORBIDDEN", "Do not have permission to access.");
    }
}
