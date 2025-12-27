package com.lilamaris.capstone.domain.model.common.domain.event.actor;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

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
