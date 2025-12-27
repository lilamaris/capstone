package com.lilamaris.capstone.domain.model.common.domain.event.actor;

import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public record UserActor(ExternalizableId userId) implements CanonicalActor {
    @Override
    public ActorType type() {
        return ActorType.USER;
    }

    @Override
    public ExternalizableId id() {
        return userId;
    }
}
