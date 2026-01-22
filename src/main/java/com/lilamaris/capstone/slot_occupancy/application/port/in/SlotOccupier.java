package com.lilamaris.capstone.slot_occupancy.application.port.in;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

public interface SlotOccupier {
    SlotOccupancyEntry occupy(SlotId slotId, SnapshotId snapshotId);
}
