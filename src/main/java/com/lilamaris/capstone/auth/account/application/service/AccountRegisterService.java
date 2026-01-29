package com.lilamaris.capstone.auth.account.application.service;

import com.lilamaris.capstone.auth.account.application.port.out.AccountPort;
import com.lilamaris.capstone.auth.account.domain.Account;
import com.lilamaris.capstone.auth.account.domain.InternalProviderType;
import com.lilamaris.capstone.auth.account.domain.id.AccountId;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthAccountEntry;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthAccountRegistrar;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthProviderTranslator;
import com.lilamaris.capstone.auth.user.domain.id.UserId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRegisterService implements
        AuthAccountRegistrar {
    private final AccountPort accountPort;
    private final AuthProviderTranslator translator;
    private final IdGenerationDirectory ids;
    private final DomainRefResolverDirectory refs;

    @Override
    public AuthAccountEntry register(
            ExternalizableId externalUserId,
            String email,
            String passwordHash
    ) {
        var userId = refs.resolve(externalUserId, AggregateDomainType.USER, UserId.class);
        var account = Account.create(
                ids.next(AccountId.class),
                userId,
                InternalProviderType.PASSWORD.name(),
                email,
                passwordHash
        );

        var created = accountPort.save(account);

        return AuthAccountEntry.from(created);
    }

    @Override
    public AuthAccountEntry register(
            ExternalizableId externalUserId,
            AuthProvider authProvider,
            String principalId
    ) {
        var userId = refs.resolve(externalUserId, AggregateDomainType.USER, UserId.class);
        var providerIdentity = translator.translate(authProvider);

        var account = Account.create(
                ids.next(AccountId.class),
                userId,
                providerIdentity.identityProvider(),
                principalId
        );

        var created = accountPort.save(account);

        return AuthAccountEntry.from(created);
    }
}
