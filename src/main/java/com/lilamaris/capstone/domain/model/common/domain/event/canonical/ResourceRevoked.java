package com.lilamaris.capstone.domain.model.common.domain.event.canonical;

import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;

import java.time.Instant;

public record ResourceRevoked(
        DomainRef ref,
        CanonicalActor actor,
        CanonicalActor revokee,
        Instant occurredAt
) implements CanonicalEvent {
}
