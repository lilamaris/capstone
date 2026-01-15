package com.lilamaris.capstone.account.application.service;

import com.lilamaris.capstone.account.application.port.out.AccountPort;
import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.ProviderType;
import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.scenario.auth.application.port.out.AccountEntry;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthAccountRegistrar;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.scenario.auth.application.port.out.ProviderTranslator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRegisterService implements
        AuthAccountRegistrar
{
    private final AccountPort accountPort;
    private final ProviderTranslator translator;
    private final IdGenerationDirectory ids;
    private final DomainRefResolverDirectory refs;

    @Override
    public AccountEntry register(
            ExternalizableId externalUserId,
            String email,
            String passwordHash
    ) {
        var userId = refs.resolve(externalUserId, AggregateDomainType.USER, UserId.class);
        var account = Account.create(
                ids.next(AccountId.class),
                userId,
                email,
                passwordHash
        );

        var created = accountPort.save(account);

        return AccountEntry.from(created);
    }

    @Override
    public AccountEntry register(
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

        return AccountEntry.from(created);
    }
}
