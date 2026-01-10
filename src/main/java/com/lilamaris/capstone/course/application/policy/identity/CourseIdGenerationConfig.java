package com.lilamaris.capstone.course.application.policy.identity;

import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.course.domain.id.CourseOfferId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class CourseIdGenerationConfig {
    @Bean
    public IdGenerator<CourseId> courseIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(CourseId.class, CourseId::new, uuid);
    }

    @Bean
    public IdGenerator<CourseOfferId> courseOfferIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(CourseOfferId.class, CourseOfferId::new, uuid);
    }
}
