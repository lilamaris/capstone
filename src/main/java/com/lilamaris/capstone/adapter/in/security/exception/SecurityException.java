package com.lilamaris.capstone.adapter.in.security.exception;

import lombok.Getter;

@Getter
public class SecurityException extends RuntimeException {
    private final String code;

    public SecurityException(String code, String message) {
        super(message);
        this.code = code;
    }
}
