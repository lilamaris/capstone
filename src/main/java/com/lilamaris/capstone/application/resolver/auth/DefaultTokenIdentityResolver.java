package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultTokenIdentityResolver implements TokenIdentityResolver {
    private final UserPort userPort;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    public AuthIdentity resolve(RefreshToken.Id id) {
        var consumed = refreshTokenPort.consume(id);
        var user = userPort.getById(consumed.userId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", consumed.userId().value())
        ));

        return new AuthIdentity(user, null, false);
    }
}
