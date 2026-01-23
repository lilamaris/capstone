package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

public interface SlotOccupier {
    OccupancyEntry occupy(SlotId slotId, SnapshotId snapshotId);
}
