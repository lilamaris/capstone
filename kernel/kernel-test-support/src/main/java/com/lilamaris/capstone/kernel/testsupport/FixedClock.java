package com.lilamaris.capstone.kernel.testsupport;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class FixedClock {
    public static final Instant REFERENCE_TIME = Instant.parse("2025-01-01T00:00:00Z");
    public static final ZoneOffset REFERENCE_ZONE = ZoneOffset.UTC;

    public static Clock getFixed() {
        return Clock.fixed(REFERENCE_TIME, REFERENCE_ZONE);
    }

    public static Clock getFixed(Instant at) {
        return Clock.fixed(at, REFERENCE_ZONE);
    }

    public static Clock getFixed(Instant at, ZoneOffset offset) {
        return Clock.fixed(at, offset);
    }
}
