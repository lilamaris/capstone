package com.lilamaris.capstone.slot_occupancy.application.port.in;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

public interface SlotOccupancyCreator {
    SlotOccupancyEntry create(SlotId slotId, SnapshotId snapshotId);
}
