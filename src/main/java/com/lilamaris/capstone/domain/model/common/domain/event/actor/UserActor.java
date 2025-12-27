package com.lilamaris.capstone.domain.model.common.domain.event.actor;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public record UserActor(ExternalizableId id) implements CanonicalActor {
    public static UserActor of(String id) {
        return new UserActor(new DefaultExternalizableId(id));
    }

    public static UserActor of(ExternalizableId id) {
        return new UserActor(id);
    }

    @Override
    public ActorType type() {
        return ActorType.USER;
    }

    @Override
    public ExternalizableId id() {
        return id;
    }
}
