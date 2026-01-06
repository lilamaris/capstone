package com.lilamaris.capstone.timeline.application.policy.identity;

import com.lilamaris.capstone.shared.application.identity.contract.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.contract.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
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
    public IdGenerator<SnapshotSlotId> snapshotSlotIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotSlotId.class, SnapshotSlotId::new, uuid);
    }
}
