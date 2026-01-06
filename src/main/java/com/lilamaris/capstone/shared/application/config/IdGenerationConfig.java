package com.lilamaris.capstone.shared.application.config;

import com.lilamaris.capstone.shared.application.identity.contract.IdGenerationContext;
import com.lilamaris.capstone.shared.application.identity.contract.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.contract.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.shared.application.identity.defaults.OpaqueTokenRawGenerator;
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
