package com.lilamaris.capstone.academiccatalog.domain.shared;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;
import java.time.Instant;

public record SimpleTemporalRange(
        Instant startAt,
        Instant endAt
) implements TemporalRange {
    public SimpleTemporalRange {
        TemporalRange.validate(startAt, endAt);
    }

    public static SimpleTemporalRange of(Instant startAt, Instant endAt) {
        return new SimpleTemporalRange(startAt, endAt);
    }

    public static SimpleTemporalRange of(Instant startAt, Duration duration) {
        Preconditions.requireNonNull(startAt, "startAt");
        Preconditions.requirePositive(duration, "duration");

        var endAt = startAt.plus(duration);
        return of(startAt, endAt);
    }

    public static SimpleTemporalRange from(TemporalRange range) {
        Preconditions.requireNonNull(range, "range");

        return of(range.startAt(), range.endAt());
    }
}
