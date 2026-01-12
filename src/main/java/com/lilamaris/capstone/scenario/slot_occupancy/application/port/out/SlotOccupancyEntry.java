package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;

public record SlotOccupancyEntry(
        DomainRef snapshotSlotRef,
        DomainRef snapshotRef
) {
    public static SlotOccupancyEntry from(SlotOccupancy slotOccupancy) {
        return new SlotOccupancyEntry(
                slotOccupancy.getSnapshotSlotId().ref(),
                slotOccupancy.getSnapshotId().ref()
        );
    }
}
