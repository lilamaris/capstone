package com.lilamaris.capstone.shared.domain.event.actor;

import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

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
