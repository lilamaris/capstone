package com.lilamaris.capstone.application.config.identity.generator;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.capstone.course.id.CourseOfferId;
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
