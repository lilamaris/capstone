package com.lilamaris.capstone.shared.application.policy.resource.membership.defaults;

import com.lilamaris.capstone.shared.application.policy.resource.membership.port.in.MembershipResolver;
import com.lilamaris.capstone.shared.application.policy.resource.membership.port.out.MembershipQuery;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultMembershipResolver implements MembershipResolver {
    private final MembershipQuery membershipQuery;

    @Override
    public List<DomainRef> getActivated(DomainType type, CanonicalActor actor) {
        return membershipQuery.getActivated(type, actor);
    }

    @Override
    public List<DomainRef> getSuspended(DomainType type, CanonicalActor actor) {
        return membershipQuery.getSuspended(type, actor);
    }

    @Override
    public List<DomainRef> getInvited(DomainType type, CanonicalActor actor) {
        return membershipQuery.getInvited(type, actor);
    }
}
