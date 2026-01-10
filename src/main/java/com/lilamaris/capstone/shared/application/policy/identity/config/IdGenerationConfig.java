package com.lilamaris.capstone.shared.application.policy.identity.config;

import com.lilamaris.capstone.shared.application.policy.identity.defaults.DefaultIdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.identity.defaults.OpaqueTokenRawGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.RawGenerator;
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
    public IdGenerationDirectory idGenerationContext(
            List<IdGenerator<?>> generators
    ) {
        return new DefaultIdGenerationDirectory(generators);
    }
}
