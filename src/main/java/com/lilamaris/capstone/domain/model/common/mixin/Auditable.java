package com.lilamaris.capstone.domain.model.common.mixin;

import java.time.Instant;

public interface Auditable {
    Instant createdAt();

    Instant updatedAt();
}
