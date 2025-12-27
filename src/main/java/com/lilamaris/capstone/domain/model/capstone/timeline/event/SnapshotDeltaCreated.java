package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.common.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;

import java.time.Instant;

public record SnapshotDeltaCreated(
        SnapshotDeltaId id,
        DomainRef resourceRef,
        Instant occurredAt
) implements AggregateEvent {
}
