package com.lilamaris.capstone.academiccatalog.application.shared.exception;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import lombok.Getter;

@Getter
public class AcademicCatalogApplicationException extends ApplicationBaseException {
    private final AcademicCatalogApplicationErrorCode errorCode;

    public AcademicCatalogApplicationException(AcademicCatalogApplicationErrorCode errorCode) {
        super(Preconditions.requireNonNull(errorCode, "errorCode"));
        this.errorCode = errorCode;
    }
}
