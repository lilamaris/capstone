package com.lilamaris.capstone.shared.infrastructure.security.authn.oidc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2ProfileMapperRegistry {
    private final List<OAuth2ProfileMapper> mappers;

    public OAuth2ProfileMapper findBy(String registrationId) {
        return mappers.stream()
                .filter(m -> m.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported OAuth2 provider: " + registrationId
                ));
    }
}
