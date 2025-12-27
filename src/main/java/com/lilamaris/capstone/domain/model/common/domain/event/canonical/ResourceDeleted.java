package com.lilamaris.capstone.domain.model.common.domain.event.canonical;

import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;

import java.time.Instant;

public record ResourceDeleted(
        DomainRef ref,
        CanonicalActor actor,
        Instant occurredAt
) implements CanonicalEvent {
}
