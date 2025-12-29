package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotSlotOccupied(
        SnapshotSlotId id,
        SnapshotId snapshotId,
        Instant occurredAt
) implements AggregateEvent {
}