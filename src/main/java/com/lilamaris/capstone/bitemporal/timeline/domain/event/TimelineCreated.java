package com.lilamaris.capstone.bitemporal.timeline.domain.event;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record TimelineCreated(
        TimelineId id,
        Instant occurredAt
) implements AggregateEvent {
}
