package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.domain.user.Provider;

import java.util.function.Function;

public interface AuthCommandUseCase {
    AuthResult.Token credentialSignIn(String email, Function<String, Boolean> challengeFunction);
    AuthResult.Token oidcSignIn(Provider provider, String providerId, String email, String displayName);

    AuthResult.Token credentialRegister(String email, String passwordHash, String displayName);

    AuthResult.Token refresh(String refreshToken);
}