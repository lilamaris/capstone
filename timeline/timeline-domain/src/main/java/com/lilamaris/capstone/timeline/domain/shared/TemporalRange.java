package com.lilamaris.capstone.timeline.domain.shared;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;
import java.time.Instant;

public interface TemporalRange {
    static void validate(Instant startAt, Instant endAt) {
        Preconditions.requireNonNull(startAt, "startAt");
        Preconditions.requireNonNull(endAt, "endAt");

        if (!startAt.isBefore(endAt)) throw new IllegalArgumentException("startAt must be before endAt.");
    }

    Instant startAt();

    Instant endAt();

    default Duration duration() {
        return Duration.between(startAt(), endAt());
    }

    default boolean contains(Instant at) {
        Preconditions.requireNonNull(at, "at");

        return !at.isBefore(startAt()) && at.isBefore(endAt());
    }

    default boolean contains(TemporalRange other) {
        Preconditions.requireNonNull(other, "other");

        return !startAt().isAfter(other.startAt()) && !endAt().isBefore(other.endAt());
    }

    default boolean containsBy(TemporalRange other) {
        Preconditions.requireNonNull(other, "other");

        return !startAt().isBefore(other.startAt()) && !endAt().isAfter(other.endAt());
    }

    default boolean overlaps(TemporalRange other) {
        Preconditions.requireNonNull(other, "other");

        return startAt().isBefore(other.endAt()) && other.startAt().isBefore(endAt());
    }

    default boolean isSame(TemporalRange other) {
        Preconditions.requireNonNull(other, "other");

        return startAt().equals(other.startAt()) && endAt().equals(other.endAt());
    }
}
