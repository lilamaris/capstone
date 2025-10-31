package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
public record Effective(
        LocalDateTime from,
        LocalDateTime to
) implements EffectiveConvertible {
    public static final LocalDateTime MAX = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    @Override
    public Effective convert() {
        return this;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public Effective {
        from = Optional.ofNullable(from).orElseGet(Effective::now);
        to =  Optional.ofNullable(to).orElse(MAX);
        validate(from, to);
    }

    public static Effective from(LocalDateTime from, LocalDateTime to) {
        return builder().from(from).to(to).build();
    }

    public static Effective openAt(LocalDateTime at) { return Effective.builder().from(at).build(); }

    public static Effective mergeRange(List<Effective> periodList) {
        if (periodList == null || periodList.isEmpty()) {
            throw new IllegalArgumentException("Period list must not be empty");
        }

        var sorted = periodList.stream().sorted(Comparator.comparing(Effective::from)).toList();
        return from(sorted.getFirst().from(), sorted.getLast().to());
    }

    public static void validate(LocalDateTime from, LocalDateTime to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' is must be after than 'from'");
        }
    }

    @Builder
    public record Split(Effective left, Effective right) {}

    public Split splitAt(LocalDateTime at) {
        var left = toBuilder().to(at).build();
        var right = toBuilder().from(at).build();
        return Split.builder().left(left).right(right).build();
    }

    public Effective copyAfterAt(LocalDateTime at) {
        return toBuilder().from(at).to(to).build();
    }

    public Effective copyBeforeAt(LocalDateTime at) {
        return toBuilder().from(from).to(at).build();
    }

    public EffectiveConvertible toConvertible() {
        return this;
    }

    public boolean isOverlap(Effective other) {
        return from.isBefore(other.to()) && to.isAfter(other.from());
    }

    public boolean contains(LocalDateTime time) {
        return from.isBefore(time) && to.isAfter(time);
    }

    public boolean isOpen() {
        return to.isEqual(MAX);
    }
}