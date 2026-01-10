package com.lilamaris.capstone.membership.application.port.out;

import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.Optional;

public interface MembershipPort {
    Optional<Membership> getById(MembershipId id);

    Optional<Membership> getMembership(CanonicalActor actorRef, DomainRef resourceRef);

    Membership save(Membership domain);

    void delete(MembershipId id);
}
