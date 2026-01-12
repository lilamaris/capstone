package com.lilamaris.capstone.snapshot.application.policy;

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
import com.lilamaris.capstone.snapshot.application.policy.previlege.SnapshotAction;
import com.lilamaris.capstone.snapshot.application.policy.previlege.SnapshotRole;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotDeltaId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.UUID;

public class SnapshotPolicyConfig {
    @Bean
    public IdGenerator<SnapshotId> snapshotIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(SnapshotId.class, SnapshotId::new, uuidRawGenerator);
    }

    @Bean
    public IdGenerator<SnapshotDeltaId> snapshotDeltaIdIdGenerator(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        return new RawBasedIdGenerator<>(SnapshotDeltaId.class, SnapshotDeltaId::new, uuidRawGenerator);
    }

    @Bean
    public DomainRefResolver<SnapshotId> snapshotIdDomainRefResolver(
            RawParser<UUID> uuidRawParser
    ) {
        return new DefaultDomainRefResolver<>(AggregateDomainType.SNAPSHOT, uuidRawParser, SnapshotId::new);
    }

    @Bean
    public DomainRoleGraphDefinition<SnapshotRole> snapshotRoleDomainRoleGraphDefinition() {
        var definition = new DefaultDomainRoleGraphDefinition<>(AggregateDomainType.SNAPSHOT, SnapshotRole.class);
        definition.extend(SnapshotRole.CONTRIBUTOR, SnapshotRole.MEMBER);
        definition.extend(SnapshotRole.MAINTAINER, SnapshotRole.CONTRIBUTOR);
        definition.setOwner(SnapshotRole.MAINTAINER);
        return definition;
    }

    @Bean
    public ResourceAccessPolicy snapshotResourceAccessPolicy() {
        var policy = new DefaultResourceAccessPolicy(AggregateDomainType.SNAPSHOT);
        policy.allow(SnapshotAction.UPDATE_METADATA, Set.of(SnapshotRole.CONTRIBUTOR));
        return policy;
    }
}
