package com.lilamaris.capstone.user.application.policy;

import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.DefaultDomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawParser;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.user.domain.id.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class UserPolicyConfig {
    @Bean
    public IdGenerator<UserId> userIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(UserId.class, UserId::new, uuid);
    }

    @Bean
    public DomainRefResolver<UserId> userIdDomainRefResolver(
            RawParser<UUID> uuidRawParser
    ) {
        return new DefaultDomainRefResolver<>(AggregateDomainType.USER, uuidRawParser, UserId::new);
    }
}
