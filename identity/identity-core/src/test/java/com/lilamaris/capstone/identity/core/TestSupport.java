package com.lilamaris.capstone.identity.core;

import com.lilamaris.capstone.identity.core.actor.Capability;
import com.lilamaris.capstone.identity.core.actor.CapabilityFixture;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.RoleCapabilities;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;

import java.util.Set;

public final class TestSupport {
    public static final String INITIAL_NAMESPACE_NAME = "test-namespace";

    public static final Capability guestCapability = CapabilityFixture.createCapability("identity.guest", "guest capability");
    public static final Capability userCapability = CapabilityFixture.createCapability("identity.user", "user capability");
    public static final Capability maintainerCapability = CapabilityFixture.createCapability("identity.maintainer", "maintainer capability");
    public static final Capability adminCapability = CapabilityFixture.createCapability("identity.admin", "admin capability");

    public static final RoleCapabilities guestRoleCapability = RoleCapabilities.of(CanonicalRole.GUEST, Set.of(guestCapability));
    public static final RoleCapabilities userRoleCapability = RoleCapabilities.of(CanonicalRole.USER, Set.of(userCapability));
    public static final RoleCapabilities maintainerRoleCapability = RoleCapabilities.of(CanonicalRole.MAINTAINER, Set.of(maintainerCapability));
    public static final RoleCapabilities adminRoleCapability = RoleCapabilities.of(CanonicalRole.ADMIN, Set.of(adminCapability));

    private TestSupport() {
    }

    public static ApplicationNamespace createApplicationNamespace() {
        return createApplicationNamespace(INITIAL_NAMESPACE_NAME);
    }

    public static ApplicationNamespace createApplicationNamespace(String name) {
        return SimpleApplicationNamespace.of(name);
    }

}
