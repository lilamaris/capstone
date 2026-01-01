package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.time.Instant;

public record SnapshotSlotOccupied(
        SnapshotSlotId id,
        SnapshotId snapshotId,
        Instant occurredAt
) implements AggregateEvent {
}