package com.lilamaris.capstone.domain.embed;

import com.lilamaris.capstone.application.config.ApplicationContext;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Effective(
        Instant from,
        Instant to
) {
    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");

    public Effective {
        if (from == null) throw new DomainIllegalArgumentException("Field 'from' must not be null.");
        if (to == null) throw new DomainIllegalArgumentException("Field 'to' must not be null.");
        if (to.isBefore(from)) throw new DomainIllegalArgumentException("Field 'to' must not be before 'from'.");
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
        return from.isBefore(other.to) && other.from.isBefore(to);
    }


    public boolean contains(Instant time) {
        return !time.isBefore(from) && time.isBefore(to);
    }

    public boolean isOpen() {
        return to.equals(MAX);
    }

    public Effective mergeRange(Effective other) {
        Instant minFrom = from.isBefore(other.from) ? from : other.from;
        Instant maxTo = to.isAfter(other.to) ? to : other.to;
        return new Effective(minFrom, maxTo);
    }

    public Split splitAt(Instant at) {
        var left = new Effective(from, at);
        var right = new Effective(at, to);
        return Split.builder().left(left).right(right).build();
    }

    @Builder
    public record Split(Effective left, Effective right) {
    }
}