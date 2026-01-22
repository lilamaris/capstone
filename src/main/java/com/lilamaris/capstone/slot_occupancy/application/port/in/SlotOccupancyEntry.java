package com.lilamaris.capstone.slot_occupancy.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

import javax.annotation.Nullable;
import java.util.Optional;

public record SlotOccupancyEntry(
        DomainRef slotRef,
        @Nullable DomainRef snapshotRef
) {
    public static SlotOccupancyEntry from(SlotOccupancy slotOccupancy) {
        return new SlotOccupancyEntry(
                slotOccupancy.getSlotId().ref(),
                Optional.ofNullable(slotOccupancy.getSnapshotId())
                        .map(SnapshotId::ref)
                        .orElse(null)
        );
    }
}
