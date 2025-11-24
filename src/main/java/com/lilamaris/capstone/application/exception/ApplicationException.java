package com.lilamaris.capstone.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final String code;

    protected ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
