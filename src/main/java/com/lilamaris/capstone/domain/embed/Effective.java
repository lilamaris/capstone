package com.lilamaris.capstone.domain.embed;

import lombok.Builder;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record Effective(
        ZonedDateTime from,
        ZonedDateTime to
) {
    public static final ZonedDateTime MAX = ZonedDateTime.of(9999, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

    public Effective {
        Objects.requireNonNull(from, "'from' of type ZonedDateTime cannot be null");
        Objects.requireNonNull(to, "'to' of type ZonedDateTime cannot be null");
        validate(from, to);
    }

    public static Effective from(ZonedDateTime from, ZonedDateTime to) {
        return builder().from(from).to(to).build();
    }

    public static void validate(ZonedDateTime from, ZonedDateTime to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' is must be after than 'from'");
        }
    }

    public Effective copyBeforeAt(ZonedDateTime at) {
        return toBuilder().from(from).to(at).build();
    }

    public boolean isOverlap(Effective other) {
        return from.isBefore(other.to()) && to.isAfter(other.from());
    }


    public boolean contains(ZonedDateTime time) {
        return from.isBefore(time) && to.isAfter(time);
    }

    public boolean isOpen() {
        return to.isEqual(MAX);
    }

    public Effective mergeRange(Effective other) {
        ZonedDateTime minFrom = from.isBefore(other.from) ? from : other.from;
        ZonedDateTime maxTo = to.isAfter(other.to) ? to : other.to;
        return new Effective(minFrom, maxTo);
    }

    @Builder
    public record Split(Effective left, Effective right) {}

    public Split splitAt(ZonedDateTime at) {
        var left = new Effective(from, at);
        var right = new Effective(at, to);
        return Split.builder().left(left).right(right).build();
    }
}