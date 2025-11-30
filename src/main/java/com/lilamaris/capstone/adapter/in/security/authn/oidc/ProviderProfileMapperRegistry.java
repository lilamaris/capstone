package com.lilamaris.capstone.adapter.in.security.authn.oidc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProviderProfileMapperRegistry {
    private final List<ProviderProfileMapper> mappers;

    public ProviderProfileMapper findBy(String registrationId) {
        return mappers.stream()
                .filter(m -> m.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported OAuth2 provider: " + registrationId
                ));
    }
}
