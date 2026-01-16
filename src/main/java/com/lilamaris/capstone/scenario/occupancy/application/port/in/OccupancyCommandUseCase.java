package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

public interface OccupancyCommandUseCase {
    OccupancyResult.Command occupySlot(SlotId slotId, SnapshotId snapshotId);
}
