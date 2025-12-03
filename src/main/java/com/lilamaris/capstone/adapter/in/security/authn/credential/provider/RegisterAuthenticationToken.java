package com.lilamaris.capstone.adapter.in.security.authn.credential.provider;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class RegisterAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credential;
    private final String displayName;

    public RegisterAuthenticationToken(String email, String rawPassword, String displayName) {
        super(null);
        this.principal = email;
        this.credential = rawPassword;
        this.displayName = displayName;
        setAuthenticated(false);
    }

    public RegisterAuthenticationToken(SecurityUserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.credential = null;
        this.displayName = principal.getDisplayName();
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
