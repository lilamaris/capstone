package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

import java.time.Instant;

public record SlotCreated(
        SlotId id,
        Instant occurredAt
) implements AggregateEvent {
}