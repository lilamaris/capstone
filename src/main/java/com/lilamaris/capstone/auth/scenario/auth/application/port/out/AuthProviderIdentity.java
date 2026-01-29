package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public record AuthProviderIdentity(
        boolean internal,
        String identityProvider
) {
}
