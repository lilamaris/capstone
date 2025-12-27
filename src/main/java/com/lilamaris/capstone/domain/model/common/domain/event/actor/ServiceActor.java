package com.lilamaris.capstone.domain.model.common.domain.event.actor;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public record ServiceActor(String serviceName) implements CanonicalActor {
    @Override
    public ActorType type() {
        return ActorType.SERVICE;
    }

    @Override
    public ExternalizableId id() {
        return new DefaultExternalizableId(serviceName);
    }
}
