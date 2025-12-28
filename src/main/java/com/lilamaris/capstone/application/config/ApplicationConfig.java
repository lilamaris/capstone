package com.lilamaris.capstone.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.application.util.generator.OpaqueTokenRawGenerator;
import com.lilamaris.capstone.application.util.policy.DefaultAuthorizer;
import com.lilamaris.capstone.application.util.policy.DefaultRoleTranslator;
import com.lilamaris.capstone.application.util.policy.DomainAuthorizer;
import com.lilamaris.capstone.application.util.policy.RoleTranslator;
import com.lilamaris.capstone.application.util.policy.timeline.TimelineAction;
import com.lilamaris.capstone.application.util.policy.timeline.TimelinePolicy;
import com.lilamaris.capstone.application.util.policy.timeline.TimelineRole;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.domain.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.domain.id.RawGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.UUID;

@Configuration
public class ApplicationConfig {
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public RawGenerator<UUID> uuidRawGenerator() {
        return UUID::randomUUID;
    }

    @Bean("opaque")
    public RawGenerator<String> opaqueTokenGenerator() {
        return new OpaqueTokenRawGenerator();
    }

    @Bean
    public IdGenerationContext idGenerationContext(
            @Qualifier("opaque") RawGenerator<String> opaqueTokenGenerator,
            RawGenerator<UUID> uuidRawGenerator
    ) {
        var registry = new DefaultIdGenerationContext();

        registry.register(TimelineId.class, TimelineId::new, uuidRawGenerator);
        registry.register(SnapshotId.class, SnapshotId::new, uuidRawGenerator);
        registry.register(SnapshotLinkId.class, SnapshotLinkId::new, uuidRawGenerator);
        registry.register(SnapshotDeltaId.class, SnapshotDeltaId::new, uuidRawGenerator);

        registry.register(RefreshTokenId.class, RefreshTokenId::new, opaqueTokenGenerator);
        registry.register(UserId.class, UserId::new, uuidRawGenerator);
        registry.register(AccountId.class, AccountId::new, uuidRawGenerator);

        registry.register(AccessControlId.class, AccessControlId::new, uuidRawGenerator);
        return registry;
    }

    @Bean
    public TimelinePolicy timelinePolicy() {
        var resolver = new TimelinePolicy();
        resolver.allow(TimelineRole.VIEWER, Set.of(TimelineAction.READ));
        resolver.allow(TimelineRole.CONTRIBUTOR, Set.of(TimelineAction.UPDATE_METADATA));
        resolver.allow(TimelineRole.MAINTAINER, Set.of(TimelineAction.MIGRATE, TimelineAction.MERGE));
        resolver.extend(TimelineRole.CONTRIBUTOR, TimelineRole.VIEWER);
        resolver.extend(TimelineRole.MAINTAINER, TimelineRole.CONTRIBUTOR);

        return resolver;
    }

    @Bean
    public RoleTranslator timelineRoleTranslator() {
        return new DefaultRoleTranslator<>(TimelineRole.class);
    }

    @Bean
    public DomainAuthorizer timelineAuthorizer(
            AccessControlPort port,
            TimelinePolicy registry,
            RoleTranslator timelineRoleTranslator
    ) {
        return new DefaultAuthorizer(port, registry, timelineRoleTranslator);
    }
}
