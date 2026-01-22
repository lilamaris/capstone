package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EffectiveResult(OffsetDateTime from, OffsetDateTime to) {
    public static EffectiveResult from(EffectiveMetadata domain) {
        var fromZoneAware = UniversityClock.toZoneAware(domain.from());
        var toZoneAware = UniversityClock.toZoneAware(domain.to());
        return builder().from(fromZoneAware).to(toZoneAware).build();
    }
}
