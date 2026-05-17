package com.lilamaris.capstone.academiccatalog.application.shared.contract;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;

public record SimpleProcessResult<PAYLOAD>(
        boolean accepted,
        ProcessReason reason,
        @Nullable PAYLOAD payload
) implements ProcessResult<PAYLOAD> {
    public SimpleProcessResult {
        ProcessResult.validate(accepted, reason);
    }

    public static <T extends ProcessReason, PAYLOAD> SimpleProcessResult<PAYLOAD> accept(T reason) {
        Preconditions.requireNonNull(reason, "reason");
        return new SimpleProcessResult<>(true, reason, null);
    }

    public static <T extends ProcessReason, PAYLOAD> SimpleProcessResult<PAYLOAD> accept(T reason, PAYLOAD payload) {
        Preconditions.requireNonNull(reason, "reason");
        Preconditions.requireNonNull(payload, "payload");
        return new SimpleProcessResult<>(true, reason, payload);
    }

    public static <T extends ProcessReason, PAYLOAD> SimpleProcessResult<PAYLOAD> reject(T reason) {
        Preconditions.requireNonNull(reason, "reason");
        return new SimpleProcessResult<>(false, reason, null);
    }
}
