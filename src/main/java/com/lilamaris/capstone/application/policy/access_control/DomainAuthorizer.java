package com.lilamaris.capstone.application.policy.access_control;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

public interface DomainAuthorizer {
    void authorize(CanonicalActor actor, DomainRef ref, DomainAction action);
}
