package com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

public interface ResourceAuthorizer {
    void authorize(CanonicalActor actor, DomainRef ref, ResourceAction actions);
}
