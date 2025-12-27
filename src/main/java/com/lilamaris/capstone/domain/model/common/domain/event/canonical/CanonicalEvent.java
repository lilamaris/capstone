package com.lilamaris.capstone.domain.model.common.domain.event.canonical;

import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;

public interface CanonicalEvent extends DomainEvent {
    CanonicalActor actor();
}
