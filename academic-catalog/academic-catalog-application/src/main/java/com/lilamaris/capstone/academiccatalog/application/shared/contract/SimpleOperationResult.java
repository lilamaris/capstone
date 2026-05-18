package com.lilamaris.capstone.academiccatalog.application.shared.contract;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;

public record SimpleOperationResult<PAYLOAD>(
        boolean accepted,
        OperationReason reason,
        @Nullable PAYLOAD value
) implements OperationResult<PAYLOAD> {
    public SimpleOperationResult {
        OperationResult.validate(accepted, reason, value);
    }

    public static <T extends OperationReason, PAYLOAD> SimpleOperationResult<PAYLOAD> accept(T reason) {
        Preconditions.requireNonNull(reason, "reason");
        return new SimpleOperationResult<>(true, reason, null);
    }

    public static <T extends OperationReason, PAYLOAD> SimpleOperationResult<PAYLOAD> accept(T reason, PAYLOAD value) {
        Preconditions.requireNonNull(reason, "reason");
        Preconditions.requireNonNull(value, "value");
        return new SimpleOperationResult<>(true, reason, value);
    }

    public static <T extends OperationReason, PAYLOAD> SimpleOperationResult<PAYLOAD> reject(T reason) {
        Preconditions.requireNonNull(reason, "reason");
        return new SimpleOperationResult<>(false, reason, null);
    }
}
