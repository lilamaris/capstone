package com.lilamaris.capstone.shared.application.exception;

import lombok.Getter;

@Getter
public class ApplicationInvariantException extends ApplicationException {
    private final String code;

    public ApplicationInvariantException(String code, String message) {
        super("APPLICATION_INVARIANT", message);
        this.code = code;
    }
}
