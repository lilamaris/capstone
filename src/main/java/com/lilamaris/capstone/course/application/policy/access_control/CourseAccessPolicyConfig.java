package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicy;
import com.lilamaris.capstone.shared.application.access_control.defaults.DefaultPolicy;
import com.lilamaris.capstone.shared.domain.type.ResourceDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class CourseAccessPolicyConfig {
    @Bean
    public DomainPolicy<CourseRole> coursePolicy() {
        var policy = new DefaultPolicy<>(ResourceDomainType.COURSE, CourseRole.class);
        policy.allow(CourseRole.VIEWER, Set.of(CourseAction.READ));
        policy.allow(CourseRole.MAINTAINER, Set.of(CourseAction.UPDATE_METADATA, CourseAction.GRANT_ROLE, CourseAction.REVOKE_ROLE));
        policy.extend(CourseRole.MAINTAINER, CourseRole.VIEWER);

        return policy;
    }
}
