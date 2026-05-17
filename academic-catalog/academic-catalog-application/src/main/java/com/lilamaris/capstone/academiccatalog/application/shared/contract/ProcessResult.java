package com.lilamaris.capstone.academiccatalog.application.shared.contract;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public interface ProcessResult<PAYLOAD> {
    static void validate(boolean accepted, ProcessReason reason) {
        Preconditions.requireNonNull(reason, "reason");

        var expectedDecision = accepted
                ? ProcessDecision.ACCEPTED
                : ProcessDecision.REJECTED;

        if (reason.decision() != expectedDecision)
            throw new IllegalArgumentException("process reason decision must match process result decision.");
    }

    boolean accepted();

    ProcessReason reason();

    PAYLOAD payload();
}
