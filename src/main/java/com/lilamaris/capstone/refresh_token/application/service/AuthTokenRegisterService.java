package com.lilamaris.capstone.refresh_token.application.service;

import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthRefreshTokenRegisterEntry;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthTokenRegistrar;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthTokenRegisterService implements
        AuthTokenRegistrar {
    private final RefreshTokenPort refreshTokenPort;
    private final IdGenerationDirectory ids;

    private final Duration refreshTokenExpiration;

    @Override
    public AuthRefreshTokenRegisterEntry register(ExternalizableId subject) {
        var id = ids.next(RefreshTokenId.class).get();
        var token = RefreshToken.create(subject);
        refreshTokenPort.save(id, token, refreshTokenExpiration);

        return new AuthRefreshTokenRegisterEntry(
                id,
                subject,
                refreshTokenExpiration
        );
    }
}
