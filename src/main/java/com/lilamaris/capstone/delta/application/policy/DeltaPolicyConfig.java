package com.lilamaris.capstone.delta.application.policy;

import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.DefaultDomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawParser;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class DeltaPolicyConfig {
    @Bean
    public IdGenerator<DeltaId> snapshotDeltaIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(DeltaId.class, DeltaId::new, uuidRawGenerator);
    }

    @Bean
    public DomainRefResolver<DeltaId> deltaIdDomainRefResolver(
            RawParser<UUID> uuidRawParser
    ) {
        return new DefaultDomainRefResolver<>(AggregateDomainType.DELTA, uuidRawParser, DeltaId::new);
    }
}
