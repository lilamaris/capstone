package com.lilamaris.capstone.user.application.policy.identity;

import com.lilamaris.capstone.shared.application.identity.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.user.domain.id.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class UserIdGenerationConfig {
    @Bean
    public IdGenerator<UserId> userIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(UserId.class, UserId::new, uuid);
    }
}
