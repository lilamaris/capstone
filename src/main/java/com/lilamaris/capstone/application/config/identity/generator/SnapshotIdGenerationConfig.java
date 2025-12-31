package com.lilamaris.capstone.application.config.identity.generator;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class SnapshotIdGenerationConfig {
    @Bean
    public IdGenerator<SnapshotId> snapshotIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotId.class, SnapshotId::new, uuid);
    }

    @Bean
    public IdGenerator<SnapshotDeltaId> snapshotDeltaIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SnapshotDeltaId.class, SnapshotDeltaId::new, uuid);
    }
}
