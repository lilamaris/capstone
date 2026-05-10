package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.actor.Capability;

import java.util.Collection;
import java.util.Set;

public interface RoleCapabilityResolver {
    Set<Capability> resolve(CanonicalRole role);

    Set<Capability> resolve(Collection<CanonicalRole> roles);
}