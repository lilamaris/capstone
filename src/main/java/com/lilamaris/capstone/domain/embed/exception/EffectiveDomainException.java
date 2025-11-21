package com.lilamaris.capstone.domain.embed.exception;

import com.lilamaris.capstone.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class EffectiveDomainException extends DomainException {
    private final EffectiveErrorCode errorCode;

    public EffectiveDomainException(EffectiveErrorCode errorCode, String message) {
        super("EFFECTIVE_" + errorCode.name(), message);
        this.errorCode = errorCode;
    }
}
