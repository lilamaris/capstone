package com.lilamaris.capstone.kernel.core.exception;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import lombok.Getter;

@Getter
public class ApplicationBaseException extends RuntimeException {
    private final ApplicationErrorCode errorCode;

    public ApplicationBaseException(ApplicationErrorCode errorCode) {
        super(Preconditions.requireNonNull(errorCode, "errorCode").getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationBaseException(ApplicationErrorCode errorCode, Throwable cause) {
        super(Preconditions.requireNonNull(errorCode, "errorCode").getMessage(), cause);
        this.errorCode = errorCode;
    }
}
