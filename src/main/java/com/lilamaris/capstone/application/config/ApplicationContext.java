package com.lilamaris.capstone.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ApplicationContext {
    private static ZoneId SYSTEM_ZONE;

    public ApplicationContext(@Value("${university.timezone}") String zone) {
        SYSTEM_ZONE = ZoneId.of(zone);
    }

    public static ZoneId getSystemZone() {
        return SYSTEM_ZONE;
    }
}
