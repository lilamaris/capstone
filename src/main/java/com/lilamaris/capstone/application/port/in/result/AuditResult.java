package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.embed.Audit;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record AuditResult(OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public static AuditResult from(Audit domain) {
        var createdAtZoneAware = UniversityClock.toZoneAware(domain.createdAt());
        var updatedAtZoneAware = UniversityClock.toZoneAware(domain.updatedAt());
        return builder().createdAt(createdAtZoneAware).updatedAt(updatedAtZoneAware).build();
    }
}
