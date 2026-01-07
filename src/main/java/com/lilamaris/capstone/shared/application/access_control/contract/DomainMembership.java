package com.lilamaris.capstone.shared.application.access_control.contract;

import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;

public interface DomainMembership {
    List<ResourceMembershipEntry> getMembershipOf(CanonicalActor actor, DomainType type);
}
