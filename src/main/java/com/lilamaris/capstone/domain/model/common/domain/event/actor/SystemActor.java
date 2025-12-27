package com.lilamaris.capstone.domain.model.common.domain.event.actor;

public enum SystemActor implements CanonicalActor {
    INSTANCE;

    @Override
    public ActorType type() {
        return ActorType.SYSTEM;
    }

    @Override
    public String identifier() {
        return "system";
    }
}
