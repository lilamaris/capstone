package com.lilamaris.capstone.snapshot.infrastructure.web.response;

import com.lilamaris.capstone.shared.infrastructure.web.response.AuditResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DescriptionResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;

public record SnapshotResponse(
        DomainRefResponse ref,
        DescriptionResponse description,
        AuditResponse audit
) {
    public static SnapshotResponse from(SnapshotEntry snapshotEntry) {
        var ref = DomainRefResponse.from(snapshotEntry.snapshotRef());
        var description = DescriptionResponse.from(snapshotEntry.descriptionMetadata());
        var audit = AuditResponse.from(snapshotEntry.auditMetadata());
        return new SnapshotResponse(
                ref,
                description,
                audit
        );
    }
}
