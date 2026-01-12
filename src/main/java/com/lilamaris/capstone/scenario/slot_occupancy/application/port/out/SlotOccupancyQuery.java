package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.util.List;
import java.util.Optional;

public interface SlotOccupancyQuery {
    List<SlotOccupancyEntry> getOccupanciesBySlotIds(List<SnapshotSlotId> ids);

    Optional<SlotOccupancyEntry> getOccupancyBySlotId(SnapshotSlotId id);
}
