package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;

public record TimelineCreated(
        TimelineId id,
        Instant occurredAt
) implements AggregateEvent {
}
