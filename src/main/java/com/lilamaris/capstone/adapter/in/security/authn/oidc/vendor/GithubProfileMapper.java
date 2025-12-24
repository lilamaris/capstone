package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OAuth2ProfileMapper;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
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
                .provider(Provider.GITHUB)
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
