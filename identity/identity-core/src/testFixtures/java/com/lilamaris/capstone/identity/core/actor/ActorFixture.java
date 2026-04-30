package com.lilamaris.capstone.identity.core.actor;

import java.util.Set;

public class ActorFixture {
    public static final String INITIAL_SUBJECT = "user-1";
    public static final Set<Capability> INITIAL_CAPABILITIES = Set.of();

    public static Actor createActor() {
        return SimpleActor.of(INITIAL_SUBJECT, INITIAL_CAPABILITIES);
    }

    public static Actor createActor(String subject, Set<Capability> capabilities) {
        return SimpleActor.of(subject, capabilities);
    }

    public static ActorBuilder builder() {
        return new ActorBuilder();
    }

    public static class ActorBuilder {
        private String subject = INITIAL_SUBJECT;
        private Set<Capability> capabilities = INITIAL_CAPABILITIES;

        public ActorBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public ActorBuilder capabilities(Set<Capability> capabilities) {
            this.capabilities = capabilities;
            return this;
        }

        public Actor build() {
            return createActor(subject, capabilities);
        }
    }
}
