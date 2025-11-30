package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.ProviderProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleProfileMapper implements ProviderProfileMapper {
    @Override
    public NormalizedProfile map(OidcUser oidcUser, OAuth2User oAuth2User, String registrationId) {
        var claims = oidcUser.getClaims();

        return new NormalizedProfile(
                Provider.GOOGLE,
                oidcUser.getSubject(),
                (String) claims.get("email"),
                (String) claims.getOrDefault("name", ""),
                oAuth2User.getAttributes(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                claims
        );
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("google");
    }
}
