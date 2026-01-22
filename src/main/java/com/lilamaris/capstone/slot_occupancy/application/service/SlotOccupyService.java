package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.slot_occupancy.application.exception.AlreadyOccupiedException;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyEntry;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyStore;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotOccupyService implements com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupier {
    private final SlotOccupancyStore slotOccupancyStore;
    private final IdGenerationDirectory ids;

    @Override
    public SlotOccupancyEntry occupy(SlotId slotId, SnapshotId snapshotId) {
        if (slotOccupancyStore.existsBySlotIdOrSnapshotId(slotId, snapshotId)) {
            throw new AlreadyOccupiedException(String.format(
                    "Slot with ref '%s' or Snapshot with ref '%s' has already occupancy", slotId, snapshotId
            ));
        }

        var slotOccupancy = SlotOccupancy.create(
                ids.next(SlotOccupancyId.class),
                slotId,
                snapshotId
        );

        var created = slotOccupancyStore.save(slotOccupancy);

        return new SlotOccupancyEntry(
                created.getSlotId().ref(),
                created.getSnapshotId().ref()
        );
    }
}
