package com.lilamaris.capstone.bitemporal.timeline.domain.event;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SlotClosureCreated(
        SlotClosureId id,
        SlotId ancestorSlotId,
        SlotId descendantSlotId,
        Instant occurredAt
) implements AggregateEvent {
}