package com.lilamaris.capstone.access_control.application.port.in;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

public interface DomainAuthorizer {
    void authorize(CanonicalActor actor, DomainRef ref, DomainAction action);
}
