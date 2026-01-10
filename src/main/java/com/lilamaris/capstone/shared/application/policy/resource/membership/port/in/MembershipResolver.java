package com.lilamaris.capstone.shared.application.policy.resource.membership.port.in;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;

public interface MembershipResolver {
    List<DomainRef> getActivated(DomainType type, CanonicalActor actor);

    List<DomainRef> getSuspended(DomainType type, CanonicalActor actor);

    List<DomainRef> getInvited(DomainType type, CanonicalActor actor);
}
