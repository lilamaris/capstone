package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotSlotUnoccupied(
        SnapshotSlotId id,
        Instant occurredAt
) implements AggregateEvent {
}