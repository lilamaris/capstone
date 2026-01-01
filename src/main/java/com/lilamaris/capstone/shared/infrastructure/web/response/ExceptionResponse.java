package com.lilamaris.capstone.shared.infrastructure.web.response;

import com.lilamaris.capstone.shared.application.exception.ApplicationException;
import lombok.Builder;

@Builder
public record ExceptionResponse(
        String code,
        String message
) {
    public static ExceptionResponse buildFromInternalException(ApplicationException e) {
        return builder().code(e.getCode()).message(e.getMessage()).build();
    }
}
