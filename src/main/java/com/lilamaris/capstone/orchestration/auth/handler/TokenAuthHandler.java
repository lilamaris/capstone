package com.lilamaris.capstone.orchestration.auth.handler;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.orchestration.auth.resolver.SessionIssuer;
import com.lilamaris.capstone.orchestration.auth.resolver.TokenIdentityResolver;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import com.lilamaris.capstone.orchestration.auth.contract.TokenAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TokenAuthHandler implements TokenAuth {
    private final TokenIdentityResolver tokenIdentityResolver;
    private final SessionIssuer sessionIssuer;

    @Override
    @Transactional
    public AuthResult.Token reissue(String refreshToken) {
        var identity = tokenIdentityResolver.resolve(new RefreshTokenId(refreshToken));
        return sessionIssuer.issue(identity);
    }
}
