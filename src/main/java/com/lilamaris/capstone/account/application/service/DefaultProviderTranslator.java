package com.lilamaris.capstone.account.application.service;

import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProviderIdentity;
import com.lilamaris.capstone.scenario.auth.application.port.out.ProviderTranslator;
import org.springframework.stereotype.Component;

@Component
public class DefaultProviderTranslator implements ProviderTranslator {
    @Override
    public AuthProviderIdentity translate(AuthProvider authProvider) {
        return switch (authProvider) {
            case CREDENTIAL -> new AuthProviderIdentity(true, "password");
            case GITHUB -> new AuthProviderIdentity(false, "github");
            case GOOGLE -> new AuthProviderIdentity(false, "google");
            case NAVER -> new AuthProviderIdentity(false, "naver");
        };
    }
}
