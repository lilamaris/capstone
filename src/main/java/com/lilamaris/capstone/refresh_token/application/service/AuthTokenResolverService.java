package com.lilamaris.capstone.refresh_token.application.service;

import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthRefreshTokenConsumeEntry;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthTokenResolver;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenResolverService implements
        AuthTokenResolver {
    private final RefreshTokenPort refreshTokenPort;

    @Override
    public AuthRefreshTokenConsumeEntry resolve(String token) {
        var consumed = refreshTokenPort.consume(new RefreshTokenId(token));
        return new AuthRefreshTokenConsumeEntry(new DefaultExternalizableId(consumed.principal()));
    }
}
