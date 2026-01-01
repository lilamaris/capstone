package com.lilamaris.capstone.shared.domain.event.actor;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface CanonicalActor {
    ActorType type();

    ExternalizableId id();

    default boolean sameIdentity(CanonicalActor other) {
        return type() == other.type()
                && id().asString().equals(other.id().asString());
    }
}
