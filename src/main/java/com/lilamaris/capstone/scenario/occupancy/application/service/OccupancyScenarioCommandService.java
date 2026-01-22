package com.lilamaris.capstone.scenario.occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyCommandUseCase;
import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupier;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OccupancyScenarioCommandService implements OccupancyCommandUseCase {
    private final SlotOccupier slotOccupier;
    private final SlotReader slotReader;
    private final SnapshotReader snapshotReader;

    @Override
    @Transactional
    public OccupancyResult.Command occupySlot(SlotId slotId, SnapshotId snapshotId) {
        var occupancy = slotOccupier.occupy(slotId, snapshotId);
        var slot = slotReader.resolveRef(occupancy.slotRef());
        var snapshot = snapshotReader.resolveRef(occupancy.snapshotRef());
        return OccupancyResult.Command.from(
                slot.tx(),
                slot.valid(),
                snapshot.ref().id()
        );
    }
}
