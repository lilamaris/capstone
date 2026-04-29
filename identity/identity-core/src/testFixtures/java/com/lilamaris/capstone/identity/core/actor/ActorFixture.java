package com.lilamaris.capstone.identity.core.actor;

import lombok.Builder;

import java.util.Set;

public class ActorFixture {
    public static final String INITIAL_SUBJECT = "user-1";
    public static final Set<Capability> INITIAL_CAPABILITIES = Set.of();

    public static Actor createActor() {
        return SimpleActor.of(INITIAL_SUBJECT, INITIAL_CAPABILITIES);
    }

    @Builder(builderClassName = "ActorBuilder", builderMethodName = "createActor")
    public static Actor createActor(String subject, Set<Capability> capabilities) {
        return SimpleActor.of(subject, capabilities);
    }

    public static class ActorBuilder {
        private String subject = INITIAL_SUBJECT;
        private Set<Capability> capabilities = INITIAL_CAPABILITIES;
    }
}
