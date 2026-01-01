package com.lilamaris.capstone.auth.application.resolver.defaults;

import com.lilamaris.capstone.auth.application.resolver.AuthIdentity;
import com.lilamaris.capstone.auth.application.resolver.TokenIdentityResolver;
import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.user.application.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultTokenIdentityResolver implements TokenIdentityResolver {
    private final UserPort userPort;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    public AuthIdentity resolve(RefreshTokenId id) {
        var consumed = refreshTokenPort.consume(id);
        var user = userPort.getById(consumed.getUserId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", consumed.getUserId())
        ));

        return new AuthIdentity(user, null, false);
    }
}
