package com.lilamaris.capstone.application.exception;

public class ApplicationInvariantException extends ApplicationException {
    public ApplicationInvariantException(String message) {
        super("APPLICATION_INVARIANT_VIOLATION", message);
    }
}
