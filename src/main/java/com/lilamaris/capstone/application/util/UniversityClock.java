package com.lilamaris.capstone.application.util;

import com.lilamaris.capstone.application.context.ApplicationContext;
import com.lilamaris.capstone.domain.embed.Effective;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class UniversityClock {
    public static Instant now() {
        return Instant.now();
    }

    public static Instant at(LocalDateTime local) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return local.atZone(systemZoneId).toInstant();
    }

    public static OffsetDateTime toZoneAware(Instant instant) {
        if (instant.equals(Effective.MAX)) {
            return instant.atOffset(ZoneOffset.UTC);
        }
        var systemZoneId = ApplicationContext.getSystemZone();
        return ZonedDateTime.ofInstant(instant, systemZoneId).toOffsetDateTime();
    }

    public static OffsetDateTime toZoneAware(LocalDateTime local) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return local.atZone(systemZoneId).toOffsetDateTime();
    }
}
