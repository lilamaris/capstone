package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

import java.time.Instant;

public record OccupancySlotEntry(
        SlotId id,
        Instant validFrom,
        Instant validTo
) {
    public static OccupancySlotEntry from(Slot slot) {
        return new OccupancySlotEntry(
                slot.id(),
                slot.getValid().getFrom(),
                slot.getValid().getTo()
        );
    }
}
