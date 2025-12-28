package com.lilamaris.capstone.application.config.identity.generator;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TimelineIdGenerationConfig {
    @Bean
    public IdGenerator<TimelineId> timelineIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, uuid);
    }

    @Bean
    public IdGenerator<SnapshotId> snapshotIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotId.class, SnapshotId::new, uuid);
    }

    @Bean
    public IdGenerator<SnapshotLinkId> snapshotLinkIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotLinkId.class, SnapshotLinkId::new, uuid);
    }

    @Bean
    public IdGenerator<SnapshotDeltaId> snapshotDeltaIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotDeltaId.class, SnapshotDeltaId::new, uuid);
    }
}
