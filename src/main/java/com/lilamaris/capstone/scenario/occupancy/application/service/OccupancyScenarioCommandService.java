package com.lilamaris.capstone.scenario.occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyCommandUseCase;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.SlotOccupancyCommand;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySnapshotQuery;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySlotQuery;
import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OccupancyScenarioCommandService implements OccupancyCommandUseCase {
    private final SlotOccupancyCommand slotOccupancyCommand;

    private final OccupancySlotQuery occupancySlotQuery;
    private final OccupancySnapshotQuery occupancySnapshotQuery;

    @Override
    @Transactional
    public OccupancyResult.Command occupySlot(SlotId slotId, SnapshotId snapshotId) {
        var occupancy = slotOccupancyCommand.occupySlot(slotId, snapshotId);
        var slot = occupancySlotQuery.getEntry(occupancy.snapshotSlotRef());
        var snapshot = occupancySnapshotQuery.getEntry(occupancy.snapshotRef());
        return OccupancyResult.Command.from(slot, snapshot);
    }
}
