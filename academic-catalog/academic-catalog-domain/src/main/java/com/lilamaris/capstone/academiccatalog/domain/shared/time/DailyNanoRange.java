package com.lilamaris.capstone.academiccatalog.domain.shared.time;


import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public interface DailyNanoRange extends
        TemporalRange<Long>,
        RangeComparable<DailyNanoRange, Long> {
    long DAY_NANOS = 24L * 60 * 60 * 1_000_000_000L;

    static void validate(long startNanoOfDay, long endNanoOfDay) {
        TemporalRange.validate(startNanoOfDay, endNanoOfDay);
        Preconditions.requireBetween(startNanoOfDay, 0L, DAY_NANOS - 1, "startNanoOfDay");
        Preconditions.requireBetween(endNanoOfDay, 1L, DAY_NANOS, "endNanoOfDay");
    }

    default LocalTime startAt() {
        return LocalTime.ofNanoOfDay(start());
    }

    default Duration duration() {
        return Duration.ofNanos(end() - start());
    }

    default boolean contains(LocalTime other) {
        Preconditions.requireNonNull(other, "other");
        return contains(other.toNanoOfDay());
    }

    default boolean contains(Instant other, ZoneId zoneId) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireNonNull(zoneId, "zoneId");

        var targetNanoOfDay = other.atZone(zoneId).toLocalTime().toNanoOfDay();

        return contains(targetNanoOfDay);
    }
}
