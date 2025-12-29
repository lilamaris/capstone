package com.lilamaris.capstone.domain.model.capstone.timeline.embed;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Effective {
    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");
    private Instant from;
    private Instant to;

    private Effective(Instant from, Instant to) {
        this.from = requireField(from, "from");
        this.to = requireField(to, "to");
        checkInvariant(from, to);
    }

    public static Effective create(Instant from, Instant to) {
        return new Effective(from, to);
    }

    public static Effective create(LocalDateTime from, LocalDateTime to, ZoneId zoneId) {
        var parsedFrom = from.atZone(zoneId).toInstant();
        var parsedTo = to.atZone(zoneId).toInstant();
        return new Effective(parsedFrom, parsedTo);
    }

    private static void checkInvariant(Instant from, Instant to) {
        if (to.isBefore(from)) throw new DomainIllegalArgumentException("Field 'to' must not be before 'from'.");
    }

    public boolean isOpen() {
        return to.equals(MAX);
    }

    public boolean isOverlap(Effective other) {
        return from.isBefore(other.to) && other.from.isBefore(to);
    }

    public boolean contains(Instant time) {
        return !time.isBefore(from) && time.isBefore(to);
    }

    public void open(Instant at) {
        this.from = at;
    }

    public void close(Instant at) {
        this.to = at;
    }

    public EffectiveSplit splitAt(Instant at) {
        var left = Effective.create(from, at);
        var right = Effective.create(at, to);
        return new EffectiveSplit(left, right);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Effective other
                && from.equals(other.from)
                && to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
