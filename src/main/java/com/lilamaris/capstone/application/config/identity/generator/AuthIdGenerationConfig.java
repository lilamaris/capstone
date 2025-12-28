package com.lilamaris.capstone.application.config.identity.generator;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AuthIdGenerationConfig {
    @Bean
    public IdGenerator<RefreshTokenId> refreshTokenIdIdGenerator(@Qualifier("opaque") RawGenerator<String> opaque) {
        return new RawBasedIdGenerator<>(RefreshTokenId.class, RefreshTokenId::new, opaque);
    }

    @Bean
    public IdGenerator<AccessControlId> accessControlIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(AccessControlId.class, AccessControlId::new, uuid);
    }

    @Bean
    public IdGenerator<AccountId> accountIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(AccountId.class, AccountId::new, uuid);
    }
}
