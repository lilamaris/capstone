package com.lilamaris.capstone.shared.domain.event.canonical;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.time.Instant;

public record ResourceRevoked(
        DomainRef ref,
        CanonicalActor actor,
        CanonicalActor revokee,
        Instant occurredAt
) implements CanonicalEvent {
}
