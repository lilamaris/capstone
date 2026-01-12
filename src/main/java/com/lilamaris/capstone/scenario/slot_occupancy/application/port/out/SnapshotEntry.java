package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.snapshot.domain.Snapshot;

public record SnapshotEntry(
        ExternalizableId id,
        DescriptionResult description
) {
    public static SnapshotEntry from(Snapshot snapshot) {
        return new SnapshotEntry(snapshot.id(), DescriptionResult.from(snapshot));
    }
}
