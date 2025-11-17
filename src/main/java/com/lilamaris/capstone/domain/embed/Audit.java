package com.lilamaris.capstone.domain.embed;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Audit(
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static Audit from(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return getDefaultBuilder(createdAt, updatedAt).build();
    }

    private static AuditBuilder getDefaultBuilder(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return builder().createdAt(createdAt).updatedAt(updatedAt);
    }
}
