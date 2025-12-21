package com.lilamaris.capstone.domain.model.capstone.timeline.embed;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Effective {
    private Instant from;
    private Instant to;

    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");

    public static Effective create(Instant from, Instant to) {
        return new Effective(from, to);
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

    public Effective closeAndNext(Instant at) {
        var split = splitAt(at);
        apply(split.left());
        return split.right();
    }

    public void apply(Effective other) {
        from = other.from;
        to = other.to;
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

    private Effective(Instant from, Instant to) {
        checkInvariant(from, to);
        this.from = from;
        this.to = to;
    }

    private static void checkInvariant(Instant from, Instant to) {
        if (from == null) throw new DomainIllegalArgumentException("Field 'from' must not be null.");
        if (to == null) throw new DomainIllegalArgumentException("Field 'to' must not be null.");
        if (to.isBefore(from)) throw new DomainIllegalArgumentException("Field 'to' must not be before 'from'.");
    }
}
