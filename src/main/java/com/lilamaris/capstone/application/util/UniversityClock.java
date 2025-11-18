package com.lilamaris.capstone.application.util;

import com.lilamaris.capstone.application.configuration.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Component
public class UniversityClock {
    public ZonedDateTime now() {
        var systemZoneId = ApplicationContext.getSystemZone();
        return ZonedDateTime.now(systemZoneId);
    }

    public ZonedDateTime at(LocalDateTime local) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return local.atZone(systemZoneId);
    }
}
