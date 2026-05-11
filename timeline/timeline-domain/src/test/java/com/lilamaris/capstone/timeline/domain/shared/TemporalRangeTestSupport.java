package com.lilamaris.capstone.timeline.domain.shared;

import com.lilamaris.capstone.kernel.testsupport.FixedClock;

import java.time.Clock;
import java.time.Instant;

public class TemporalRangeTestSupport {
    private static final Clock clock = FixedClock.getFixed();

    public static Instant START_AT = clock.instant();
    public static Instant END_AT = START_AT.plusSeconds(10);
    public static Instant BEFORE_END_AT = END_AT.minusSeconds(3);
    public static Instant AFTER_END_AT = END_AT.plusSeconds(3);
    public static Instant BEFORE_START_AT = START_AT.minusSeconds(3);
    public static Instant AFTER_START_AT = START_AT.plusSeconds(3);
}
