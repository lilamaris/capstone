package com.lilamaris.capstone.account.application.service;

import com.lilamaris.capstone.account.application.port.out.AccountPort;
import com.lilamaris.capstone.account.domain.ProviderType;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthAccountEntry;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthAccountResolver;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProviderTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountResolverService implements
        AuthAccountResolver
{
    private final AccountPort accountPort;
    private final AuthProviderTranslator translator;

    @Override
    public Optional<AuthAccountEntry> resolve(AuthProvider provider, String principalId) {
        var providerIdentity = translator.translate(provider);
        var providerType = providerIdentity.internal() ? ProviderType.INTERNAL : ProviderType.EXTERNAL;

        return accountPort.getBy(providerType, providerIdentity.identityProvider(), principalId)
                .map(AuthAccountEntry::from);
    }
}
