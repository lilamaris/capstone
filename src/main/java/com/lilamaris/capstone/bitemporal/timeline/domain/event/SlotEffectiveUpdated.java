package com.lilamaris.capstone.bitemporal.timeline.domain.event;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;

import java.time.Instant;

public record SlotEffectiveUpdated(
        SlotId id,
        EffectiveMetadata tx,
        EffectiveMetadata valid,
        Instant occurredAt
) implements AggregateEvent {
}