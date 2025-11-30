package com.lilamaris.capstone.adapter.in.security.authn.oidc;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ProviderProfileMapper {
    boolean supports(String registrationId);

    NormalizedProfile map(OidcUser oidcUser, OAuth2User oAuth2User, String registrationId);
}
