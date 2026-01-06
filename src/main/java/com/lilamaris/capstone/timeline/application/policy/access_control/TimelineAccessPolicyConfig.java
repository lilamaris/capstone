package com.lilamaris.capstone.timeline.application.policy.access_control;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicy;
import com.lilamaris.capstone.shared.application.access_control.defaults.DefaultPolicy;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class TimelineAccessPolicyConfig {
    @Bean
    public DomainPolicy<TimelineRole> timelinePolicy() {
        var policy = new DefaultPolicy<>(CoreDomainType.TIMELINE, TimelineRole.class);
        policy.allow(TimelineRole.VIEWER, Set.of(TimelineAction.READ));
        policy.allow(TimelineRole.CONTRIBUTOR, Set.of(TimelineAction.UPDATE_METADATA));
        policy.allow(TimelineRole.MAINTAINER, Set.of(TimelineAction.MIGRATE, TimelineAction.MERGE));
        policy.extend(TimelineRole.CONTRIBUTOR, TimelineRole.VIEWER);
        policy.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);

        return policy;
    }
}
