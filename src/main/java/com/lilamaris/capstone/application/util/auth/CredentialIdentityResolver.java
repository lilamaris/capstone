package com.lilamaris.capstone.application.util.auth;

import java.util.function.Function;

public interface CredentialIdentityResolver {
    AuthIdentity resolve(String email, Function<String, Boolean> challengeFunction);
}
