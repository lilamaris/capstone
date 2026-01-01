package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.event.actor.ActorType;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record DefaultActor(ActorType type, ExternalizableId id) implements CanonicalActor {
}
