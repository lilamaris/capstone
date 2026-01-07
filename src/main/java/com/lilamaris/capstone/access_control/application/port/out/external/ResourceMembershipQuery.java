package com.lilamaris.capstone.access_control.application.port.out.external;

import com.lilamaris.capstone.shared.application.access_control.contract.ResourceMembershipEntry;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;

public interface ResourceMembershipQuery {
    List<ResourceMembershipEntry> getMembershipByType(CanonicalActor actor, DomainType type);
}
