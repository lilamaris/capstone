package com.lilamaris.capstone.domain.model.common.domain.event.canonical;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

import java.time.Instant;

public record ResourceGranted(
        DomainRef ref,
        CanonicalActor actor,
        CanonicalActor to,
        Instant occurredAt
) implements CanonicalEvent {
}
