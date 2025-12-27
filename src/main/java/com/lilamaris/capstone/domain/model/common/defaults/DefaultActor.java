package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.ActorType;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

import java.util.Objects;

public record DefaultActor(ActorType type, ExternalizableId id) implements CanonicalActor {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanonicalActor other)) return false;
        return type.equals(other.type())
                && id().asString().equals(other.id().asString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id().asString());
    }
}
