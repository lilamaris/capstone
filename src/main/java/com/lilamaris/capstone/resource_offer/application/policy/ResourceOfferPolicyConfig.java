package com.lilamaris.capstone.resource_offer.application.policy;

import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ResourceOfferPolicyConfig {
    @Bean
    IdGenerator<ResourceOfferId> resourceOfferIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(ResourceOfferId.class, ResourceOfferId::new, uuidRawGenerator);
    }
}
