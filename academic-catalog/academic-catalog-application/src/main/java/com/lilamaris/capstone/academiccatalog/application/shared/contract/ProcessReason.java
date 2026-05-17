package com.lilamaris.capstone.academiccatalog.application.shared.contract;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;

public interface ProcessReason extends ApplicationErrorCode {
    ProcessDecision decision();
}
