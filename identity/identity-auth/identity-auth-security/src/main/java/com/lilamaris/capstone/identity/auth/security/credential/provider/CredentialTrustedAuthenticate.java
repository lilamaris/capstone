package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.security.shared.principal.TrustedPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CredentialTrustedAuthenticate extends AbstractAuthenticationToken implements TrustedPrincipal {
    private final AuthenticationResult authenticationResult;
    private final Set<String> scopes;

    private CredentialTrustedAuthenticate(
            AuthenticationResult authenticationResult,
            Collection<? extends GrantedAuthority> authorities,
            Set<String> scopes
    ) {
        super(authorities);
        super.setAuthenticated(true);

        this.authenticationResult = authenticationResult;
        this.scopes = scopes;
    }

    public static CredentialTrustedAuthenticate of(AuthenticationResult authenticationResult, Set<String> scopes) {
        var authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
        return new CredentialTrustedAuthenticate(authenticationResult, authorities, scopes);
    }

    @Override
    public Object getPrincipal() {
        return authenticationResult;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UUID userId() {
        return authenticationResult.userId();
    }

    @Override
    public String nickname() {
        return authenticationResult.nickname();
    }

    @Override
    public Set<String> scopes() {
        return scopes;
    }
}
