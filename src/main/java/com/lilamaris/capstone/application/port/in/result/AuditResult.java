package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.model.common.domain.contract.Auditable;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record AuditResult(OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public static AuditResult from(Auditable domain) {
        var audit = domain.auditMetadata();
        var createdAtZoneAware = UniversityClock.toZoneAware(audit.createdAt());
        var updatedAtZoneAware = UniversityClock.toZoneAware(audit.updatedAt());
        return builder().createdAt(createdAtZoneAware).updatedAt(updatedAtZoneAware).build();
    }
}
