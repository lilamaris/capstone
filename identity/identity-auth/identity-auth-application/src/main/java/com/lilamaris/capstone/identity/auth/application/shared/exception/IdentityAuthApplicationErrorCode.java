package com.lilamaris.capstone.identity.auth.application.shared.exception;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IdentityAuthApplicationErrorCode implements ApplicationErrorCode {
    AUTHENTICATION_FAILED("A001", "인증 실패"),
    USER_NOT_FOUND("A002", "사용자가 존재하지 않습니다."),
    ACCOUNT_NOT_FOUND("A003", "계정이 존재하지 않습니다."),
    ACCOUNT_ALREADY_EXISTS("A004", "계정이 이미 존재합니다."),
    CREDENTIAL_EMAIL_DUPLICATED("A005", "동일한 이메일이 이미 존재합니다.");

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
