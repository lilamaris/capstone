package com.lilamaris.capstone.shared.domain.event.canonical;

import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;

public interface CanonicalEvent extends DomainEvent {
    CanonicalActor actor();
}
