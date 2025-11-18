package com.lilamaris.capstone.domain.embed;

import com.lilamaris.capstone.application.configuration.ApplicationContext;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record Effective(
        Instant from,
        Instant to
) {
    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");

    public Effective {
        Objects.requireNonNull(from, "'from' of type Instant cannot be null");
        Objects.requireNonNull(to, "'to' of type Instant cannot be null");
        validate(from, to);
    }

    public static void validate(Instant from, Instant to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' is must be after than 'from'");
        }
    }


    public static Effective from(Instant from, Instant to) {
        return new Effective(from, to);
    }

    public static Effective from(LocalDateTime from, LocalDateTime to) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return from(from.atZone(systemZoneId).toInstant(), to.atZone(systemZoneId).toInstant());
    }

    public static Effective openAt(Instant at) {
        return from(at, Effective.MAX);
    }

    public static Effective openAt(LocalDateTime at) {
        var systemZoneId = ApplicationContext.getSystemZone();
        return openAt(at.atZone(systemZoneId).toInstant());
    }

    public Effective closeAt(Instant at) {
        return toBuilder().from(from).to(at).build();
    }

    public boolean isOverlap(Effective other) {
        return from.isBefore(other.to()) && to.isAfter(other.from());
    }


    public boolean contains(Instant time) {
        return from.isBefore(time) && to.isAfter(time);
    }

    public boolean isOpen() {
        return to.equals(MAX);
    }

    public Effective mergeRange(Effective other) {
        Instant minFrom = from.isBefore(other.from) ? from : other.from;
        Instant maxTo = to.isAfter(other.to) ? to : other.to;
        return new Effective(minFrom, maxTo);
    }

    @Builder
    public record Split(Effective left, Effective right) {}

    public Split splitAt(Instant at) {
        var left = new Effective(from, at);
        var right = new Effective(at, to);
        return Split.builder().left(left).right(right).build();
    }
}