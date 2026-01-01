package com.lilamaris.capstone.shared.domain.event.canonical;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.time.Instant;

public record ResourceGranted(
        DomainRef ref,
        CanonicalActor actor,
        CanonicalActor grantee,
        String scopedRole,
        Instant occurredAt
) implements CanonicalEvent {
}
