package com.lilamaris.capstone.domain.model.capstone.snapshot.event;

import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

import java.time.Instant;

public record SnapshotDeltaCreated(
        SnapshotDeltaId id,
        DomainRef resourceRef,
        Instant occurredAt
) implements AggregateEvent {
}
