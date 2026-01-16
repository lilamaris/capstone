package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

public interface SlotOccupancyCommand {
    SlotOccupancyEntry occupySlot(SlotId slotId, SnapshotId snapshotId);
}
