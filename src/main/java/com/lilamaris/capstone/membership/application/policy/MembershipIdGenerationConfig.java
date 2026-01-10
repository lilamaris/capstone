package com.lilamaris.capstone.membership.application.policy;

import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MembershipIdGenerationConfig {
    @Bean
    public IdGenerator<MembershipId> membershipIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(MembershipId.class, MembershipId::new, uuid);
    }
}
