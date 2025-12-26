package com.lilamaris.capstone.domain.model.common.event;

import com.lilamaris.capstone.domain.model.common.event.actor.CanonicalActor;

public interface CanonicalEvent extends DomainEvent {
    CanonicalActor actor();
}
