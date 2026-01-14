package com.lilamaris.capstone.scenario.auth.application.port.out;

public record AuthProviderIdentity(
        boolean internal,
        String identityProvider
) {
}
