package com.lilamaris.capstone.domain.model.common.domain.event.actor;

public record ServiceActor(String serviceName) implements CanonicalActor {
    @Override
    public ActorType type() {
        return ActorType.SERVICE;
    }

    @Override
    public String identifier() {
        return serviceName;
    }
}
