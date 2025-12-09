package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OidcProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class GoogleProfileMapper implements OidcProfileMapper {
    @Override
    public NormalizedProfile map(OidcUser oidcUser, String registrationId) {
        var claims = oidcUser.getClaims();

        return new NormalizedProfile(
                Provider.GOOGLE,
                oidcUser.getSubject(),
                (String) claims.get("email"),
                (String) claims.getOrDefault("name", ""),
                oidcUser.getAttributes(),
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
