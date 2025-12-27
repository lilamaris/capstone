package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotCreated(
        SnapshotId id,
        Instant occurredAt
) implements AggregateEvent {
}