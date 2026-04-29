package com.lilamaris.capstone.identity.core.actor;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.Set;

public record SimpleActor(
        String subject,
        Set<Capability> capabilities
) implements Actor {
    public SimpleActor {
        subject = Preconditions.requireNonBlank(subject, "subject");
        capabilities = Set.copyOf(Preconditions.requireNonNull(capabilities, "capabilities"));
    }

    public static SimpleActor of(String subject, Set<Capability> capabilities) {
        return new SimpleActor(subject, capabilities);
    }

    public static SimpleActor from(Actor actor) {
        Preconditions.requireNonNull(actor, "actor");
        return of(actor.subject(), actor.capabilities());
    }
}
