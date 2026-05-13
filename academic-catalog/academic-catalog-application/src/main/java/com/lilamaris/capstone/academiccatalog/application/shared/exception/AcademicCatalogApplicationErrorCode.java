package com.lilamaris.capstone.academiccatalog.application.shared.exception;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AcademicCatalogApplicationErrorCode implements ApplicationErrorCode {
    TIMELINE_NOT_FOUND("A001", "타임라인을 찾을 수 없습니다.");
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
