package com.lilamaris.capstone.shared.domain.metadata;

import java.time.Instant;

public interface AuditMetadata {
    Instant createdAt();

    Instant updatedAt();
}
