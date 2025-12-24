package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
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
