package com.lilamaris.capstone.shared.application.access_control.defaults;

import com.lilamaris.capstone.access_control.application.port.out.external.ResourceMembershipQuery;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainMembership;
import com.lilamaris.capstone.shared.application.access_control.contract.ResourceMembershipEntry;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultMembership implements DomainMembership {
    private final ResourceMembershipQuery resourceMembershipQuery;

    public List<ResourceMembershipEntry> getMembershipOf(CanonicalActor actor, DomainType type) {
        return resourceMembershipQuery.getMembershipByType(actor, type);
    }
}
