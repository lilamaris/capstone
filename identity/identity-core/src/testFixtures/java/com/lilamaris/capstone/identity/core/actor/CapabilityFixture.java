package com.lilamaris.capstone.identity.core.actor;

import lombok.Builder;

public class CapabilityFixture {
    public static final String INITIAL_SCOPE = "test.scope";
    public static final String INITIAL_DESCRIPTION = "test scope description.";

    public static Capability createCapability() {
        return SimpleCapability.of(INITIAL_SCOPE, INITIAL_DESCRIPTION);
    }

    @Builder(builderClassName = "CapabilityBuilder", builderMethodName = "builder")
    public static Capability createCapability(String scope, String description) {
        return SimpleCapability.of(scope, description);
    }

    public static class CapabilityBuilder {
        private String scope = INITIAL_SCOPE;
        private String description = INITIAL_DESCRIPTION;
    }
}
