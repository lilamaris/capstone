package com.lilamaris.capstone.domain.model.capstone.snapshot.event;

import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotCreated(
        SnapshotId id,
        Instant occurredAt
) implements AggregateEvent {
}