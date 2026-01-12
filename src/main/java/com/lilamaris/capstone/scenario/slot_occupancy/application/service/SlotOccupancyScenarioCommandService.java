package com.lilamaris.capstone.scenario.slot_occupancy.application.service;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.in.SlotOccupancyCommandUseCase;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SlotOccupancyCommand;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotQuery;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotSlotQuery;
import com.lilamaris.capstone.scenario.slot_occupancy.application.result.SlotOccupancyResult;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SlotOccupancyScenarioCommandService implements SlotOccupancyCommandUseCase {
    private final SlotOccupancyCommand slotOccupancyCommand;

    private final SnapshotSlotQuery snapshotSlotQuery;
    private final SnapshotQuery snapshotQuery;

    @Override
    @Transactional
    public SlotOccupancyResult.Command occupySlot(SnapshotSlotId slotId, SnapshotId snapshotId) {
        var occupancy = slotOccupancyCommand.occupySlot(slotId, snapshotId);
        var slot = snapshotSlotQuery.getEntry(occupancy.snapshotSlotRef());
        var snapshot = snapshotQuery.getEntry(occupancy.snapshotRef());
        return SlotOccupancyResult.Command.from(slot, snapshot);
    }
}
