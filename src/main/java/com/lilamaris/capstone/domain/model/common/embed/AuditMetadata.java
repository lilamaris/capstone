package com.lilamaris.capstone.domain.model.common.embed;

import java.time.Instant;

public interface AuditMetadata {
    Instant createdAt();

    Instant updatedAt();
}
