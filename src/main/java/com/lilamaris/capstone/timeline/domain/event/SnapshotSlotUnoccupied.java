package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.time.Instant;

public record SnapshotSlotUnoccupied(
        SnapshotSlotId id,
        Instant occurredAt
) implements AggregateEvent {
}