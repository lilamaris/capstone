package com.lilamaris.capstone.domain.model.common.domain.event.actor;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public enum SystemActor implements CanonicalActor {
    INSTANCE;

    private final ExternalizableId id = new DefaultExternalizableId("system");

    @Override
    public ActorType type() {
        return ActorType.SYSTEM;
    }

    @Override
    public ExternalizableId id() {
        return id;
    }
}
