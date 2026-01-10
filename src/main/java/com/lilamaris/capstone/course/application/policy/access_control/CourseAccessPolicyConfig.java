package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.policy.resource.access_control.defaults.DefaultResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.domain.type.ResourceDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class CourseAccessPolicyConfig {
    @Bean
    public ResourceAccessPolicy coursePolicy() {
        var policy = new DefaultResourceAccessPolicy(ResourceDomainType.COURSE);
        policy.allow(CourseAction.READ, Set.of(CourseRole.VIEWER));
        policy.allow(CourseAction.UPDATE_METADATA, Set.of(CourseRole.MAINTAINER));
        policy.allow(CourseAction.GRANT_ROLE, Set.of(CourseRole.MAINTAINER));
        policy.allow(CourseAction.REVOKE_ROLE, Set.of(CourseRole.MAINTAINER));
        return policy;
    }
}
