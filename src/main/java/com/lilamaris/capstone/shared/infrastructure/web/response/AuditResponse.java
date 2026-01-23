package com.lilamaris.capstone.shared.infrastructure.web.response;

import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;

import java.time.Instant;

public record AuditResponse(
        Instant createdAt,
        Instant updatedAt
) {
    public static AuditResponse from(AuditMetadata audit) {
        return new AuditResponse(
                audit.createdAt(),
                audit.updatedAt()
        );
    }
}
