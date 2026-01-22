package com.lilamaris.capstone.timeline.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

import java.time.Instant;

public record SlotEffectiveUpdated(
        SlotId id,
        EffectiveMetadata tx,
        EffectiveMetadata valid,
        Instant occurredAt
) implements AggregateEvent {
}