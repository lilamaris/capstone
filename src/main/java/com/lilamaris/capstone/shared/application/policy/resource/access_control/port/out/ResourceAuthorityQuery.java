package com.lilamaris.capstone.shared.application.policy.resource.access_control.port.out;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.Optional;

public interface ResourceAuthorityQuery {
    Optional<ResourceAuthorityEntry> getAuthorization(CanonicalActor actor, DomainRef ref);
}
