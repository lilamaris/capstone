package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.ProviderProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GithubProfileMapper implements ProviderProfileMapper {
    @Override
    public NormalizedProfile map(OidcUser oidcUser, OAuth2User oAuth2User, String registrationId) {
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
                oidcUser != null ? oidcUser.getIdToken() : null,
                oidcUser != null ? oidcUser.getUserInfo() : null,
                oidcUser != null ? oidcUser.getClaims() : Map.of()
        );
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("github");
    }
}
