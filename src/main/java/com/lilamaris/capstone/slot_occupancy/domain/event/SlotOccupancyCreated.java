package com.lilamaris.capstone.slot_occupancy.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;

import java.time.Instant;

public record SlotOccupancyCreated(
        SlotOccupancyId id,
        Instant occurredAt
) implements AggregateEvent {
}
