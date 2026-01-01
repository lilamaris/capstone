package com.lilamaris.capstone.timeline.application.result;

import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EffectiveResult(OffsetDateTime from, OffsetDateTime to) {
    public static EffectiveResult from(Effective domain) {
        var fromZoneAware = UniversityClock.toZoneAware(domain.getFrom());
        var toZoneAware = UniversityClock.toZoneAware(domain.getTo());
        return builder().from(fromZoneAware).to(toZoneAware).build();
    }
}
