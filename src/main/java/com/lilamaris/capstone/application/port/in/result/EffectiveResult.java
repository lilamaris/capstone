package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EffectiveResult (OffsetDateTime from, OffsetDateTime to) {
    public static EffectiveResult from(Effective domain) {
        var fromZoneAware = UniversityClock.toZoneAware(domain.getFrom());
        var toZoneAware = UniversityClock.toZoneAware(domain.getTo());
        return builder().from(fromZoneAware).to(toZoneAware).build();
    }
}
