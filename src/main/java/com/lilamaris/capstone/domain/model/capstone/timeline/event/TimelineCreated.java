package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.event.aggregate.AggregateEvent;

import java.time.Instant;

public record TimelineCreated(
        TimelineId id,
        Instant occurredAt
) implements AggregateEvent {
}
