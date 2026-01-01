package com.lilamaris.capstone.orchestration.auth.resolver;

import java.util.function.Function;

public interface CredentialIdentityResolver {
    AuthIdentity resolve(String email, Function<String, Boolean> challengeFunction);
}
