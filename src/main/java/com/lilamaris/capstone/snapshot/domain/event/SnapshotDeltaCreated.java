package com.lilamaris.capstone.snapshot.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotDeltaId;

import java.time.Instant;

public record SnapshotDeltaCreated(
        SnapshotDeltaId id,
        DomainRef resourceRef,
        Instant occurredAt
) implements AggregateEvent {
}
