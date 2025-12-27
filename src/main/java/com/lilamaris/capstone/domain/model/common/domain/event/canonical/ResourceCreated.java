package com.lilamaris.capstone.domain.model.common.domain.event.canonical;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

import java.time.Instant;

public record ResourceCreated(
        DomainRef ref,
        CanonicalActor actor,
        Instant occurredAt
) implements CanonicalEvent {
}
