package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;
import java.time.Instant;

public record SimpleInstantRange(
        Instant start,
        Instant end
) implements InstantRange {
    public SimpleInstantRange {
        InstantRange.validate(start, end);
    }

    public static SimpleInstantRange of(Instant startAt, Instant endAt) {
        return new SimpleInstantRange(startAt, endAt);
    }

    public static SimpleInstantRange of(Instant startAt, Duration duration) {
        Preconditions.requireNonNull(startAt, "startAt");
        Preconditions.requirePositive(duration, "duration");

        var endAt = startAt.plus(duration);
        return of(startAt, endAt);
    }

    public static SimpleInstantRange from(InstantRange range) {
        Preconditions.requireNonNull(range, "range");

        return of(range.start(), range.end());
    }
}
