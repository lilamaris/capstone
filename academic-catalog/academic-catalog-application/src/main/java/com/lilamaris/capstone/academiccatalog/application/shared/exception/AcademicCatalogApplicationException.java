package com.lilamaris.capstone.academiccatalog.application.shared.exception;

import com.lilamaris.capstone.academiccatalog.application.shared.contract.OperationDecision;
import com.lilamaris.capstone.academiccatalog.application.shared.contract.OperationReason;
import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import lombok.Getter;

@Getter
public class AcademicCatalogApplicationException extends ApplicationBaseException {
    private final ApplicationErrorCode errorCode;

    public AcademicCatalogApplicationException(ApplicationErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public AcademicCatalogApplicationException(OperationReason reason) {
        super(reason);
        if (reason.decision() != OperationDecision.REJECTED) {
            throw new IllegalArgumentException("exception reason must be rejected.");
        }
        this.errorCode = reason;
    }
}
