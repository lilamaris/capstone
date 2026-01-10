package com.lilamaris.capstone.snapshot.application.policy.identity;

import com.lilamaris.capstone.shared.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.RawGenerator;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotDeltaId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
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
