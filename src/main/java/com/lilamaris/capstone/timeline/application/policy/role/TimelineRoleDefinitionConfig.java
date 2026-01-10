package com.lilamaris.capstone.timeline.application.policy.role;

import com.lilamaris.capstone.shared.application.policy.role.defaults.DefaultRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleGraphDefinition;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimelineRoleDefinitionConfig {
    @Bean
    public RoleGraphDefinition<TimelineRole> timelineRoleRoleGraphDefinition() {
        var graph = new DefaultRoleGraphDefinition<>(CoreDomainType.TIMELINE, TimelineRole.class);
        graph.extend(TimelineRole.CONTRIBUTOR, TimelineRole.MEMBER);
        graph.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);
        graph.setOwner(TimelineRole.MAINTAINER);
        return graph;
    }
}
