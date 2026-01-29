package com.lilamaris.capstone.bitemporal.timeline.domain.event;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SlotCreated(
        SlotId id,
        Instant occurredAt
) implements AggregateEvent {
}