package com.lilamaris.capstone.adapter.in.security;

import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUserDetails implements UserDetails, OidcUser, OAuth2User {
    private String userId;
    private String displayName;
    private Role role;

    private Provider provider;
    private String providerId;

    // OAuth2 specific
    private Map<String, Object> attributes;

    // OIDC specific
    private OidcIdToken idToken;
    private Map<String, Object> claims;
    private OidcUserInfo userInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes != null ? attributes : Map.of();
    }

    @Override
    public String getName() {
        return userId != null ? userId : displayName;
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims != null ? claims : Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}
