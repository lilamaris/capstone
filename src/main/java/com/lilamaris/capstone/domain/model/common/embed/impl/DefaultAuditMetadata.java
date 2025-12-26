package com.lilamaris.capstone.domain.model.common.embed.impl;

import com.lilamaris.capstone.domain.model.common.embed.AuditMetadata;

import java.time.Instant;

public record DefaultAuditMetadata(Instant createdAt, Instant updatedAt) implements AuditMetadata {
}
