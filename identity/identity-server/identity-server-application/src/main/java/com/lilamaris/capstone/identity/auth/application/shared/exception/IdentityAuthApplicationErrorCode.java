package com.lilamaris.capstone.identity.auth.application.shared.exception;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IdentityAuthApplicationErrorCode implements ApplicationErrorCode {
    AUTHENTICATION_FAILED("A001", "인증 실패"),
    USER_NOT_FOUND("A002", "사용자가 존재하지 않습니다."),
    ACCOUNT_NOT_FOUND("A003", "계정이 존재하지 않습니다."),
    ACCOUNT_ALREADY_EXISTS("A004", "계정이 이미 존재합니다."),
    CREDENTIAL_EMAIL_DUPLICATED("A005", "동일한 이메일이 이미 존재합니다."),
    UNAUTHORIZED_GRANT_ATTEMPT("A006", "허용되지 않은 권한 부여입니다."),
    UNAUTHORIZED_REVOKE_ATTEMPT("A007", "허용되지 않은 권한 회수입니다."),
    USER_GRANT_ALREADY_EXISTS("A008", "사용자에게 이미 부여된 권한입니다."),
    USER_GRANT_NOT_FOUND("A009", "사용자 권한을 찾을 수 없습니다.");

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
