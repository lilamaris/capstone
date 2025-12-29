package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotSlotEffectiveUpdated(
        SnapshotSlotId id,
        Effective tx,
        Effective valid,
        Instant occurredAt
) implements AggregateEvent {
}