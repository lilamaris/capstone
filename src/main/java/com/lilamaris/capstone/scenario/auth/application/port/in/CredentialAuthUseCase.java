package com.lilamaris.capstone.scenario.auth.application.port.in;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;

import java.util.function.Function;

public interface CredentialAuthUseCase {
    AuthResult.Token signIn(String email, Function<String, Boolean> challengeFunction);

    AuthResult.Token register(String email, String passwordHash, String displayName);
}
