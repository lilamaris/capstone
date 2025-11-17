package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.embed.Audit;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuditResult(LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static AuditResult from(Audit domain) {
        return builder().createdAt(domain.createdAt()).updatedAt(domain.updatedAt()).build();
    }
}
