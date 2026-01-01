package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.time.Instant;

public record SnapshotSlotEffectiveUpdated(
        SnapshotSlotId id,
        Effective tx,
        Effective valid,
        Instant occurredAt
) implements AggregateEvent {
}