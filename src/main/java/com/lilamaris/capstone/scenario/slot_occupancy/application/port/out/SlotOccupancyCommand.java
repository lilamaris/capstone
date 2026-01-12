package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

public interface SlotOccupancyCommand {
    SlotOccupancyEntry occupySlot(SnapshotSlotId slotId, SnapshotId snapshotId);
}
