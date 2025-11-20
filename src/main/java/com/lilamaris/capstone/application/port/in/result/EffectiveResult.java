package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.embed.Effective;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EffectiveResult (OffsetDateTime from, OffsetDateTime to) {
    public static EffectiveResult from(Effective domain) {
        var fromZoneAware = UniversityClock.toZoneAware(domain.from());
        var toZoneAware = UniversityClock.toZoneAware(domain.to());
        return builder().from(fromZoneAware).to(toZoneAware).build();
    }
}
