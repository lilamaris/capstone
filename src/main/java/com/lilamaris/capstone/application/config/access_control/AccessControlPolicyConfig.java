package com.lilamaris.capstone.application.config.access_control;

import com.lilamaris.capstone.application.config.access_control.privilege.course.CourseAction;
import com.lilamaris.capstone.application.config.access_control.privilege.course.CourseRole;
import com.lilamaris.capstone.application.config.access_control.privilege.timeline.TimelineAction;
import com.lilamaris.capstone.application.config.access_control.privilege.timeline.TimelineRole;
import com.lilamaris.capstone.application.policy.access_control.DomainAuthorizer;
import com.lilamaris.capstone.application.policy.access_control.DomainPolicy;
import com.lilamaris.capstone.application.policy.access_control.DomainPolicyDirectory;
import com.lilamaris.capstone.application.policy.access_control.defaults.DefaultAuthorizer;
import com.lilamaris.capstone.application.policy.access_control.defaults.DefaultPolicy;
import com.lilamaris.capstone.application.policy.access_control.defaults.DefaultPolicyDirectory;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.model.common.domain.type.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.domain.type.ResourceDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AccessControlPolicyConfig {
    @Bean
    public DomainPolicy<TimelineRole> timelinePolicy() {
        var policy = new DefaultPolicy<>(TimelineRole.class);
        policy.allow(TimelineRole.VIEWER, Set.of(TimelineAction.READ));
        policy.allow(TimelineRole.CONTRIBUTOR, Set.of(TimelineAction.UPDATE_METADATA));
        policy.allow(TimelineRole.MAINTAINER, Set.of(TimelineAction.MIGRATE, TimelineAction.MERGE));
        policy.extend(TimelineRole.CONTRIBUTOR, TimelineRole.VIEWER);
        policy.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);

        return policy;
    }

    @Bean
    public DomainPolicy<CourseRole> coursePolicy() {
        var policy = new DefaultPolicy<>(CourseRole.class);
        policy.allow(CourseRole.VIEWER, Set.of(CourseAction.READ));
        policy.allow(CourseRole.MAINTAINER, Set.of(CourseAction.UPDATE_METADATA, CourseAction.GRANT_ROLE, CourseAction.REVOKE_ROLE));
        policy.extend(CourseRole.MAINTAINER, CourseRole.VIEWER);

        return policy;
    }

    @Bean
    public DomainPolicyDirectory domainPolicyDirectory(
            DomainPolicy<TimelineRole> timelinePolicy,
            DomainPolicy<CourseRole> coursePolicy
    ) {
        var dir = new DefaultPolicyDirectory();
        dir.addPolicy(CoreDomainType.TIMELINE, timelinePolicy);
        dir.addPolicy(ResourceDomainType.COURSE, coursePolicy);

        return dir;
    }

    @Bean
    public DomainAuthorizer authorizer(
            AccessControlPort accessControlPort,
            DomainPolicyDirectory domainPolicyDirectory
    ) {
        return new DefaultAuthorizer(accessControlPort, domainPolicyDirectory);
    }
}
