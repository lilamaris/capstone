package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import java.time.Duration;
import java.time.Instant;

public interface InstantRange extends
        TemporalRange<Instant>,
        RangeComparable<InstantRange, Instant> {
    static void validate(Instant startAt, Instant endAt) {
        TemporalRange.validate(startAt, endAt);
    }

    default Duration duration() {
        return Duration.between(start(), end());
    }
}
