package com.lilamaris.capstone.access.resource.domain.event;

import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.time.Instant;

public record ResourceCreatedEvent(
        DomainRef resource,
        Instant occurredAt
) implements AggregateEvent {
}
