package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.EffectiveSelector;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.event.AggregateEvent;

import java.time.Instant;

public record SnapshotEffectiveChanged(
        SnapshotId id,
        EffectiveSelector selector,
        Instant at,
        Instant occurredAt
) implements AggregateEvent {
}
