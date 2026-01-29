package com.lilamaris.capstone.auth.account.application.service;

import com.lilamaris.capstone.auth.account.application.port.out.AccountPort;
import com.lilamaris.capstone.auth.account.domain.ProviderType;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthAccountExistenceChecker;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthProviderTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountExistenceCheckerService implements
        AuthAccountExistenceChecker {
    private final AccountPort accountPort;
    private final AuthProviderTranslator translator;

    @Override
    public boolean isExists(AuthProvider authProvider, String principalId) {
        var providerIdentity = translator.translate(authProvider);

        var providerType = providerIdentity.internal() ? ProviderType.INTERNAL : ProviderType.EXTERNAL;

        return accountPort.isExists(
                providerType,
                providerIdentity.identityProvider(),
                principalId
        );
    }
}
