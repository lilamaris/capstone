package com.lilamaris.capstone.account.application.policy.identity;

import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.shared.application.identity.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.RawBasedIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AccountIdGenerationConfig {
    @Bean
    public IdGenerator<AccountId> accountIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(AccountId.class, AccountId::new, uuid);
    }
}
