package com.lilamaris.capstone.snapshot.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

import java.time.Instant;

public record SnapshotCreated(
        SnapshotId id,
        Instant occurredAt
) implements AggregateEvent {
}