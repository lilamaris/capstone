package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OAuth2ProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GithubProfileMapper implements OAuth2ProfileMapper {
    @Override
    public NormalizedProfile map(OAuth2User oAuth2User, String registrationId) {
        var attrs = oAuth2User.getAttributes();

        String id = String.valueOf(attrs.get("id"));
        String email = (String) attrs.get("email");
        String name = (String) attrs.getOrDefault("name", "");

        return new NormalizedProfile(
                Provider.GITHUB,
                id,
                email,
                name,
                attrs,
                null,
                null,
                null
        );
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("github");
    }
}
