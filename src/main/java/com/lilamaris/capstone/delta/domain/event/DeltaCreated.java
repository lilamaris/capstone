package com.lilamaris.capstone.delta.domain.event;

import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.time.Instant;

public record DeltaCreated(
        DeltaId id,
        DomainRef resourceRef,
        Instant occurredAt
) implements AggregateEvent {
}
