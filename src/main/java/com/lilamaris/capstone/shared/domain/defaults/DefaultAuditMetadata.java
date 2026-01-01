package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;

import java.time.Instant;

public record DefaultAuditMetadata(Instant createdAt, Instant updatedAt) implements AuditMetadata {
}
