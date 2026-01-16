package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.timeline.domain.id.SlotId;

import java.util.List;
import java.util.Optional;

public interface SlotOccupancyQuery {
    List<SlotOccupancyEntry> getOccupanciesBySlotIds(List<SlotId> ids);

    Optional<SlotOccupancyEntry> getOccupancyBySlotId(SlotId id);
}
