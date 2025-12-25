package com.lilamaris.capstone.domain.model.auth.account;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    private final IdGenerationContext idGenerationContext;

    public AccountFactory(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> map = Map.of(
                AccountId.class, DefaultIdGenerationContext.bind(AccountId::new, uuidRawGenerator)
        );

        this.idGenerationContext = new DefaultIdGenerationContext(map);
    }

    public Account create(UserId userId, String displayName, String email, String passwordHash) {
        var id = idGenerationContext.next(AccountId.class);
        return new Account(
                id,
                userId,
                Provider.LOCAL,
                email,
                displayName,
                email,
                requireField(passwordHash, "passwordHash")
        );
    }

    public Account create(UserId userId, Provider provider, String providerId, String displayName, String email) {
        if (provider.equals(Provider.LOCAL)) {
            throw new DomainIllegalArgumentException(
                    "Can not create a local account by explicitly specifying the provider."
            );
        }
        var id = idGenerationContext.next(AccountId.class);
        return new Account(
                id,
                userId,
                provider,
                providerId,
                displayName,
                email,
                null
        );
    }
}
