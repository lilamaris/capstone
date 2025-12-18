package com.lilamaris.capstone.application.exception;

public class InfrastructureFailureException extends ApplicationException {
    public InfrastructureFailureException(String message) {
        super("INFRASTRUCTURE_FAILURE", message);
    }
}
