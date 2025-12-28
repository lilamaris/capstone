package com.lilamaris.capstone.application.config.identity.generator;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
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
