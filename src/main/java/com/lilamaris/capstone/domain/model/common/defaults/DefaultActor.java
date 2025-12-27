package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.ActorType;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public record DefaultActor(ActorType type, ExternalizableId id) implements CanonicalActor {
}
