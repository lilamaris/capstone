package com.lilamaris.capstone.orchestration.auth.contract;

import com.lilamaris.capstone.orchestration.auth.result.AuthResult;

import java.util.function.Function;

public interface CredentialAuth {
    AuthResult.Token signIn(String email, Function<String, Boolean> challengeFunction);

    AuthResult.Token register(String email, String passwordHash, String displayName);
}
