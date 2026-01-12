package com.lilamaris.capstone.scenario.slot_occupancy.application.port.in;

import com.lilamaris.capstone.scenario.slot_occupancy.application.result.SlotOccupancyResult;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

public interface SlotOccupancyCommandUseCase {
    SlotOccupancyResult.Command occupySlot(SnapshotSlotId slotId, SnapshotId snapshotId);
}
