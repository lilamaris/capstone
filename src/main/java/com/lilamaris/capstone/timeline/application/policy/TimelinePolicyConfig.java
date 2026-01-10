package com.lilamaris.capstone.timeline.application.policy;

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
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import com.lilamaris.capstone.timeline.application.policy.privilege.TimelineAction;
import com.lilamaris.capstone.timeline.application.policy.privilege.TimelineRole;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.UUID;

@Configuration
public class TimelinePolicyConfig {
    @Bean
    public IdGenerator<TimelineId> timelineIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, uuidRawGenerator);
    }

    @Bean
    public IdGenerator<SnapshotSlotId> snapshotSlotIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(SnapshotSlotId.class, SnapshotSlotId::new, uuidRawGenerator);
    }

    @Bean
    public DomainRefResolver<TimelineId> timelineIdDomainRefResolver(
            RawParser<UUID> uuidRawParser
    ) {
        return new DefaultDomainRefResolver<>(CoreDomainType.TIMELINE, uuidRawParser, TimelineId::new);
    }

    @Bean
    public DomainRoleGraphDefinition<TimelineRole> timelineRoleRoleGraphDefinition() {
        var definition = new DefaultDomainRoleGraphDefinition<>(CoreDomainType.TIMELINE, TimelineRole.class);
        definition.extend(TimelineRole.CONTRIBUTOR, TimelineRole.MEMBER);
        definition.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);
        definition.setOwner(TimelineRole.MAINTAINER);
        return definition;
    }

    @Bean
    public ResourceAccessPolicy timelineRoleResourceAccessPolicy() {
        var policy = new DefaultResourceAccessPolicy(CoreDomainType.TIMELINE);
        policy.allow(TimelineAction.UPDATE_METADATA, Set.of(TimelineRole.CONTRIBUTOR));
        policy.allow(TimelineAction.MIGRATE, Set.of(TimelineRole.MAINTAINER));
        policy.allow(TimelineAction.MERGE, Set.of(TimelineRole.MAINTAINER));
        return policy;
    }
}
