package com.lilamaris.capstone.shared.domain.event.actor;

import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record ServiceActor(ExternalizableId id) implements CanonicalActor {
    public static ServiceActor of(String serviceName) {
        return new ServiceActor(new DefaultExternalizableId(serviceName));
    }

    @Override
    public ActorType type() {
        return ActorType.SERVICE;
    }

    @Override
    public ExternalizableId id() {
        return id;
    }
}
