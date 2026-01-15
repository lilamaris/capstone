package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.vendor;

import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.OAuth2ProfileMapper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GithubProfileMapper implements OAuth2ProfileMapper {
    @Override
    public NormalizedProfile map(OAuth2User oAuth2User, String registrationId) {
        var attributes = oAuth2User.getAttributes();

        var providerId = (String) attributes.get("id");
        var email = (String) attributes.get("email");
        var displayName = (String) attributes.getOrDefault("name", "");

        return NormalizedProfile.builder()
                .authProvider(AuthProvider.GITHUB)
                .providerId(providerId)
                .email(email)
                .displayName(displayName)
                .attributes(attributes)
                .build();
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("github");
    }
}
