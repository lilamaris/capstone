package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;

public record SnapshotSummaryEntry(
        DomainRef ref,
        DescriptionMetadata description
) {
    public static SnapshotSummaryEntry from(SnapshotEntry snapshotEntry) {
        return new SnapshotSummaryEntry(
                snapshotEntry.snapshotRef(),
                snapshotEntry.descriptionMetadata()
        );
    }
}