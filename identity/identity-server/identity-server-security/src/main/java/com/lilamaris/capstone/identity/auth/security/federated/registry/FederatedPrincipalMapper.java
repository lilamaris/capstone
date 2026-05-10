package com.lilamaris.capstone.identity.auth.security.federated.registry;

import com.lilamaris.capstone.identity.auth.security.federated.principal.FederatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface FederatedPrincipalMapper {
    String key();

    FederatedPrincipal resolve(OAuth2User oAuth2User);
}
