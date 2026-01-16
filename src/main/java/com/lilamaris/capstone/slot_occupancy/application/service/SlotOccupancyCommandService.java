package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.scenario.slot_occupancy.application.exception.SlotOccupancyScenarioInvariantException;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SlotOccupancyCommand;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SlotOccupancyEntry;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyPort;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotOccupancyCommandService implements
        SlotOccupancyCommand {
    private final SlotOccupancyPort slotOccupancyPort;
    private final IdGenerationDirectory ids;

    @Override
    public SlotOccupancyEntry occupySlot(SnapshotSlotId slotId, SnapshotId snapshotId) {
        if (slotOccupancyPort.existsBySlotIdOrSnapshotId(slotId, snapshotId)) {
            throw new SlotOccupancyScenarioInvariantException(String.format(
                    "Slot with id '%s' or Snapshot with id '%s' has already occupancy", slotId, snapshotId
            ));
        }

        var slotOccupancy = SlotOccupancy.create(
                ids.next(SlotOccupancyId.class),
                slotId,
                snapshotId
        );

        var created = slotOccupancyPort.save(slotOccupancy);

        return SlotOccupancyEntry.from(created);
    }
}
