package com.lilamaris.capstone.application.util;

import com.lilamaris.capstone.application.configuration.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class UniversityClock {
    public static Instant now() {
        return Instant.now();
    }

    public static Instant at(LocalDateTime local) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return local.atZone(systemZoneId).toInstant();
    }
}
