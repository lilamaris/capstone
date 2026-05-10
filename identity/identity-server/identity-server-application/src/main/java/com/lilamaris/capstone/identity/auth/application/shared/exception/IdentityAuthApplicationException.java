package com.lilamaris.capstone.identity.auth.application.shared.exception;

import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import lombok.Getter;

@Getter
public class IdentityAuthApplicationException extends ApplicationBaseException {
    private final IdentityAuthApplicationErrorCode errorCode;

    public IdentityAuthApplicationException(IdentityAuthApplicationErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
