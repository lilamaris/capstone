package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.timeline.domain.SnapshotSlot;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.time.Instant;

public record SnapshotSlotEntry(
        SnapshotSlotId id,
        Instant validFrom,
        Instant validTo
) {
    public static SnapshotSlotEntry from(SnapshotSlot snapshotSlot) {
        return new SnapshotSlotEntry(
                snapshotSlot.id(),
                snapshotSlot.getValid().getFrom(),
                snapshotSlot.getValid().getTo()
        );
    }
}
