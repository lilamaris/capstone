package com.lilamaris.capstone.auth.account.application.service;

import com.lilamaris.capstone.auth.account.application.port.out.AccountPort;
import com.lilamaris.capstone.auth.account.domain.ProviderType;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.*;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountVerifyService implements AuthVerifier {
    private final AccountPort accountPort;
    private final AuthProviderTranslator translator;
    private final PasswordEncoder encoder;

    @Override
    public AuthVerifiedAccount verify(AuthProvider authProvider, String principalId, String challenge) {
        var providerIdentity = translator.translate(authProvider);
        var providerType = providerIdentity.internal() ? ProviderType.INTERNAL : ProviderType.EXTERNAL;

        var account = accountPort.getBy(providerType, providerIdentity.identityProvider(), principalId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Account with principal '%s' not found.", principalId
                )));

        var success = encoder.matches(challenge, account.getPasswordHash());

        return new AuthVerifiedAccount(
                success,
                success ? AuthAccountEntry.from(account) : null
        );
    }
}
