package com.lilamaris.capstone.account.application.policy.identity;

import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
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
