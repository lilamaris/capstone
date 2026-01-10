package com.lilamaris.capstone.timeline.application.policy.role;

import com.lilamaris.capstone.shared.application.policy.domain.role.defaults.DefaultDomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimelineRoleDefinitionConfig {
    @Bean
    public DomainRoleGraphDefinition<TimelineRole> timelineRoleRoleGraphDefinition() {
        var graph = new DefaultDomainRoleGraphDefinition<>(CoreDomainType.TIMELINE, TimelineRole.class);
        graph.extend(TimelineRole.CONTRIBUTOR, TimelineRole.MEMBER);
        graph.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);
        graph.setOwner(TimelineRole.MAINTAINER);
        return graph;
    }
}
