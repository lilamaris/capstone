package com.lilamaris.capstone.shared.domain.event.canonical;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.time.Instant;

public record ResourceCreated(
        DomainRef ref,
        CanonicalActor actor,
        Instant occurredAt
) implements CanonicalEvent {
}
