package com.lilamaris.capstone.access_control.application.config;

import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.application.identity.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.RawBasedIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AccessControlIdGenerationConfig {
    @Bean
    public IdGenerator<AccessControlId> accessControlIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(AccessControlId.class, AccessControlId::new, uuid);
    }
}
