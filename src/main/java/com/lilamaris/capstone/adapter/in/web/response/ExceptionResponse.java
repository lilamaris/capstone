package com.lilamaris.capstone.adapter.in.web.response;

import com.lilamaris.capstone.application.exception.ApplicationException;
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
