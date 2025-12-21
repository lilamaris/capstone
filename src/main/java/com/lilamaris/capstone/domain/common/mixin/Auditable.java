package com.lilamaris.capstone.domain.common.mixin;

import java.time.Instant;

public interface Auditable {
    Instant createdAt();
    Instant updatedAt();
}
