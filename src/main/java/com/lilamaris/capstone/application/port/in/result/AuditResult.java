package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.model.common.domain.metadata.AuditMetadata;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record AuditResult(OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public static AuditResult from(AuditMetadata domain) {
        var createdAtZoneAware = UniversityClock.toZoneAware(domain.createdAt());
        var updatedAtZoneAware = UniversityClock.toZoneAware(domain.updatedAt());
        return builder().createdAt(createdAtZoneAware).updatedAt(updatedAtZoneAware).build();
    }
}
