package com.lilamaris.capstone.domain.model.common.domain.metadata;

import java.time.Instant;

public interface AuditMetadata {
    Instant createdAt();

    Instant updatedAt();
}
