package com.lilamaris.capstone.adapter.in.security.authn.oidc;

import com.lilamaris.capstone.domain.user.Provider;
import lombok.Builder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

@Builder
public record NormalizedProfile(
        Provider provider,
        String providerId,
        String email,
        String displayName,
        Map<String, Object> attributes,
        OidcIdToken idToken,
        OidcUserInfo userInfo,
        Map<String, Object> claims
) { }
