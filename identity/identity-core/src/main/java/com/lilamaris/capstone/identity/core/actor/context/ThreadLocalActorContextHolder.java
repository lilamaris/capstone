package com.lilamaris.capstone.identity.core.actor.context;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public class ThreadLocalActorContextHolder implements ActorContextHolder {
    private static final ThreadLocal<Actor> CURRENT = new ThreadLocal<>();

    @Override
    public Actor getActor() {
        return CURRENT.get();
    }

    @Override
    public void setActor(Actor actor) {
        Preconditions.requireNonNull(actor, "actor");
        CURRENT.set(actor);
    }

    @Override
    public void clear() {
        CURRENT.remove();
    }
}
