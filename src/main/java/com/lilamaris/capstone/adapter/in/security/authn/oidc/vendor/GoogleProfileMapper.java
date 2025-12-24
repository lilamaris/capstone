package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OidcProfileMapper;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class GoogleProfileMapper implements OidcProfileMapper {
    @Override
    public NormalizedProfile map(OidcUser oidcUser, String registrationId) {
        var claims = oidcUser.getClaims();

        var providerId = oidcUser.getSubject();
        var email = (String) claims.get("email");
        var displayName = (String) claims.getOrDefault("name", "");
        var attributes = oidcUser.getAttributes();
        var idToken = oidcUser.getIdToken();
        var userInfo = oidcUser.getUserInfo();

        return NormalizedProfile.builder()
                .provider(Provider.GOOGLE)
                .providerId(providerId)
                .email(email)
                .displayName(displayName)
                .attributes(attributes)
                .idToken(idToken)
                .userInfo(userInfo)
                .build();
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("google");
    }
}
