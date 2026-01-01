package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.shared.domain.contract.Auditable;

import java.time.OffsetDateTime;

public record AuditResult(OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public static AuditResult from(Auditable domain) {
        var audit = domain.auditMetadata();
        var createdAtZoneAware = UniversityClock.toZoneAware(audit.createdAt());
        var updatedAtZoneAware = UniversityClock.toZoneAware(audit.updatedAt());
        return new AuditResult(
                createdAtZoneAware,
                updatedAtZoneAware
        );
    }
}
