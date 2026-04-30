package com.lilamaris.capstone.identity.core.actor;

public class CapabilityFixture {
    public static final String INITIAL_SCOPE = "test.scope";
    public static final String INITIAL_DESCRIPTION = "test scope description.";

    public static Capability createCapability() {
        return SimpleCapability.of(INITIAL_SCOPE, INITIAL_DESCRIPTION);
    }

    public static Capability createCapability(String scope, String description) {
        return SimpleCapability.of(scope, description);
    }

    public static CapabilityBuilder builder() {
        return new CapabilityBuilder();
    }

    public static class CapabilityBuilder {
        private String scope = INITIAL_SCOPE;
        private String description = INITIAL_DESCRIPTION;

        public CapabilityBuilder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public CapabilityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Capability build() {
            return createCapability(scope, description);
        }
    }
}
