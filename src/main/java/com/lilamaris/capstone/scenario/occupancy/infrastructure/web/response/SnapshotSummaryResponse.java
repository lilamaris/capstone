package com.lilamaris.capstone.scenario.occupancy.infrastructure.web.response;


import com.lilamaris.capstone.scenario.occupancy.application.port.in.SnapshotSummaryEntry;
import com.lilamaris.capstone.shared.infrastructure.web.response.DescriptionResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;

public record SnapshotSummaryResponse(
        DomainRefResponse ref,
        DescriptionResponse description
) {
    public static SnapshotSummaryResponse from(SnapshotSummaryEntry snapshotSummaryEntry) {
        if (snapshotSummaryEntry == null) return null;
        var ref = DomainRefResponse.from(snapshotSummaryEntry.ref());
        var description = DescriptionResponse.from(snapshotSummaryEntry.description());
        return new SnapshotSummaryResponse(
                ref,
                description
        );
    }
}