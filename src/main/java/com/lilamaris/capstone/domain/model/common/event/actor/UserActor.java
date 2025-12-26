package com.lilamaris.capstone.domain.model.common.event.actor;

public record UserActor(String userId) implements CanonicalActor {
    @Override
    public ActorType type() {
        return ActorType.USER;
    }

    @Override
    public String identifier() {
        return userId;
    }
}
