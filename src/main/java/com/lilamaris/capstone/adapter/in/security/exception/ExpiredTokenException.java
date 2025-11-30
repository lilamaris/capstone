package com.lilamaris.capstone.adapter.in.security.exception;

import lombok.Getter;

@Getter
public class ExpiredTokenException extends SecurityException {
    public ExpiredTokenException(String message) {
        super("SECURITY_EXPIRED_TOKEN", message);
    }
}
