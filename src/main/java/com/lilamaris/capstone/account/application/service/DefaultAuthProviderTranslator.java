package com.lilamaris.capstone.account.application.service;

import com.lilamaris.capstone.account.domain.InternalProviderType;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProviderIdentity;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProviderTranslator;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthProviderTranslator implements AuthProviderTranslator {
    @Override
    public AuthProviderIdentity translate(AuthProvider authProvider) {
        return switch (authProvider) {
            case CREDENTIAL -> new AuthProviderIdentity(true, InternalProviderType.PASSWORD.name());
            case GITHUB -> new AuthProviderIdentity(false, "github");
            case GOOGLE -> new AuthProviderIdentity(false, "google");
            case NAVER -> new AuthProviderIdentity(false, "naver");
        };
    }
}
