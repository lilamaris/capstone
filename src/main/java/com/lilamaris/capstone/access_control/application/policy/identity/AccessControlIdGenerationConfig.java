package com.lilamaris.capstone.access_control.application.policy.identity;

import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.RawGenerator;
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
