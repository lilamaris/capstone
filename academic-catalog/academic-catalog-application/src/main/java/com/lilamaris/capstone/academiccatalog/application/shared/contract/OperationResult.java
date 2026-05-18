package com.lilamaris.capstone.academiccatalog.application.shared.contract;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.Optional;

public interface OperationResult<PAYLOAD> {
    static <PAYLOAD> void validate(boolean accepted, OperationReason reason, PAYLOAD value) {
        Preconditions.requireNonNull(reason, "reason");

        var expectedDecision = accepted
                ? OperationDecision.ACCEPTED
                : OperationDecision.REJECTED;

        if (reason.decision() != expectedDecision)
            throw new IllegalArgumentException("process reason decision must match process result decision.");

        if (!accepted && value != null) {
            throw new IllegalArgumentException("reject process result must not have payload.");
        }
    }

    boolean accepted();

    default boolean rejected() {
        return !accepted();
    }

    OperationReason reason();

    PAYLOAD value();

    default Optional<PAYLOAD> payload() {
        return Optional.ofNullable(value());
    }
}
