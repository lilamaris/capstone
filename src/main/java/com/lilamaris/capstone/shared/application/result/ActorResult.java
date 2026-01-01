package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.domain.event.actor.ActorType;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record ActorResult(ActorType type, ExternalizableId id) {
    public static ActorResult from(CanonicalActor actor) {
        return new ActorResult(
                actor.type(),
                actor.id()
        );
    }
}
