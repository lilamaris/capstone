package com.lilamaris.capstone.timeline.application.policy.identity;

import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.DefaultDomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawParser;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TimelineDomainRefResolverConfig {
    @Bean
    public DomainRefResolver<TimelineId> timelineIdDomainRefResolver(
            RawParser<UUID> uuid
    ) {
        return new DefaultDomainRefResolver<>(CoreDomainType.TIMELINE, uuid, TimelineId::new);
    }
}
