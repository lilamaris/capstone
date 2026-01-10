package com.lilamaris.capstone.shared.application.policy.identity.config;

import com.lilamaris.capstone.shared.application.policy.identity.defaults.DefaultDomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.RawParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class DomainRefConfig {
    @Bean
    public RawParser<UUID> uuidRawParser() {
        return UUID::fromString;
    }

    @Bean
    public DomainRefResolverDirectory domainRefResolverDirectory(
            List<DomainRefResolver<?>> resolvers
    ) {
        return new DefaultDomainRefResolverDirectory(resolvers);
    }
}
