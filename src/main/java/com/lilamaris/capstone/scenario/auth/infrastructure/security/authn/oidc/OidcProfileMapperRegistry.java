package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OidcProfileMapperRegistry {
    private final List<OidcProfileMapper> mappers;

    public OidcProfileMapper findBy(String registrationId) {
        return mappers.stream()
                .filter(m -> m.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported OIDC provider: " + registrationId
                ));
    }
}
