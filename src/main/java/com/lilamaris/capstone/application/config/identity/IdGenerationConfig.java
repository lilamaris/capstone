package com.lilamaris.capstone.application.config.identity;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.raw.OpaqueTokenRawGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class IdGenerationConfig {
    @Bean
    public RawGenerator<UUID> uuidRawGenerator() {
        return UUID::randomUUID;
    }

    @Bean("opaque")
    public RawGenerator<String> opaqueTokenGenerator() {
        return new OpaqueTokenRawGenerator();
    }

    @Bean
    public IdGenerationContext idGenerationContext(
            List<IdGenerator<?>> generators
    ) {
        return new DefaultIdGenerationContext(generators);
    }
}
