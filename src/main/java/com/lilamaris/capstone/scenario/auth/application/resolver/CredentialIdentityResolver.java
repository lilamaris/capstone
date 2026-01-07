package com.lilamaris.capstone.scenario.auth.application.resolver;

import java.util.function.Function;

public interface CredentialIdentityResolver {
    AuthIdentity resolve(String email, Function<String, Boolean> challengeFunction);
}
