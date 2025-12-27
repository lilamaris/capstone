package com.lilamaris.capstone.application.config;

import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;

public final class ActorContext {
    private static final ThreadLocal<CanonicalActor> CURRENT = new ThreadLocal<>();

    public static void set(CanonicalActor actor) {
        CURRENT.set(actor);
    }

    public static CanonicalActor get() {
        var actor = CURRENT.get();
        if (actor == null) {
            throw new ApplicationInvariantException("ACTOR_NOT_EXISTS", "Actor context is null.");
        }
        return actor;
    }

    public static void clear() {
        CURRENT.remove();
    }
}
