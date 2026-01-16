package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.snapshot.domain.Snapshot;

public record OccupancySnapshotEntry(
        ExternalizableId id,
        DescriptionResult description
) {
    public static OccupancySnapshotEntry from(Snapshot snapshot) {
        return new OccupancySnapshotEntry(snapshot.id(), DescriptionResult.from(snapshot));
    }
}
