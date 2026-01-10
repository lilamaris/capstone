package com.lilamaris.capstone.timeline.application.policy.access_control;

import com.lilamaris.capstone.shared.application.policy.access_control.defaults.DefaultResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import com.lilamaris.capstone.timeline.application.policy.role.TimelineRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class TimelineAccessPolicyConfig {
    @Bean
    public ResourceAccessPolicy timelineRoleResourceAccessPolicy() {
        var policy = new DefaultResourceAccessPolicy(CoreDomainType.TIMELINE);
        policy.allow(TimelineAction.UPDATE_METADATA, Set.of(TimelineRole.CONTRIBUTOR));
        policy.allow(TimelineAction.MIGRATE, Set.of(TimelineRole.MAINTAINER));
        policy.allow(TimelineAction.MERGE, Set.of(TimelineRole.MAINTAINER));
        return policy;
    }
}
