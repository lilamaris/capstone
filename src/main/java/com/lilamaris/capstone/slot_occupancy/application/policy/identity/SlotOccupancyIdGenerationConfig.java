package com.lilamaris.capstone.slot_occupancy.application.policy.identity;

import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class SlotOccupancyIdGenerationConfig {
    @Bean
    IdGenerator<SlotOccupancyId> slotOccupancyIdIdGenerator(RawGenerator<UUID> uuid) {
        return new RawBasedIdGenerator<>(SlotOccupancyId.class, SlotOccupancyId::new, uuid);
    }
}
