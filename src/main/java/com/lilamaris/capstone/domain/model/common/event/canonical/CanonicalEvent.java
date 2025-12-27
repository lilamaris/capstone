package com.lilamaris.capstone.domain.model.common.event.canonical;

import com.lilamaris.capstone.domain.model.common.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.event.actor.CanonicalActor;

public interface CanonicalEvent extends DomainEvent {
    CanonicalActor actor();
}
