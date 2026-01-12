package com.lilamaris.capstone.course.application.policy;

import com.lilamaris.capstone.course.application.policy.access_control.privilege.CourseAction;
import com.lilamaris.capstone.course.application.policy.access_control.privilege.CourseRole;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.DefaultDomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawParser;
import com.lilamaris.capstone.shared.application.policy.domain.role.defaults.DefaultDomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.defaults.DefaultResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.UUID;

@Configuration
public class CoursePolicyConfig {
    @Bean
    public IdGenerator<CourseId> courseIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(CourseId.class, CourseId::new, uuidRawGenerator);
    }

    @Bean
    public DomainRefResolver<CourseId> courseIdDomainRefResolver(
            RawParser<UUID> uuidRawParser
    ) {
        return new DefaultDomainRefResolver<>(AggregateDomainType.COURSE, uuidRawParser, CourseId::new);
    }

    @Bean
    public DomainRoleGraphDefinition<CourseRole> courseRoleDomainRoleGraphDefinition() {
        var definition = new DefaultDomainRoleGraphDefinition<>(AggregateDomainType.COURSE, CourseRole.class);
        definition.extend(CourseRole.MAINTAINER, CourseRole.VIEWER);
        return definition;
    }

    @Bean
    public ResourceAccessPolicy courseResourceAccessPolicy() {
        var policy = new DefaultResourceAccessPolicy(AggregateDomainType.COURSE);
        policy.allow(CourseAction.UPDATE_METADATA, Set.of(CourseRole.MAINTAINER));
        return policy;
    }
}
