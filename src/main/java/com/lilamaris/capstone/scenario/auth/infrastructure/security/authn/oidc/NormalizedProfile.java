package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc;

import com.lilamaris.capstone.account.domain.Provider;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class NormalizedProfile implements OidcUser, OAuth2User {
    private Provider provider;
    private String providerId;
    private String email;
    private String displayName;

    // OAuth2User field
    private Map<String, Object> attributes;

    // OidcUser field
    private OidcIdToken idToken;
    private Map<String, Object> claims;
    private OidcUserInfo userInfo;

    // OidcUser, OAuth2User required
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes != null ? attributes : Map.of();
    }

    @Override
    public String getName() {
        return provider + ":" + providerId;
    }

    // OidcUser spec
    @Override
    public Map<String, Object> getClaims() {
        return claims != null ? claims : Map.of();
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }
}