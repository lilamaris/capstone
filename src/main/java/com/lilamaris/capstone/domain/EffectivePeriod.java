package com.lilamaris.capstone.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
public record EffectivePeriod(
        LocalDateTime from,
        LocalDateTime to
) {
    public static final LocalDateTime MAX = LocalDateTime.of(9999, 12, 31, 23, 59);

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public EffectivePeriod {
        from = Optional.ofNullable(from).orElseGet(EffectivePeriod::now);
        to =  Optional.ofNullable(to).orElse(MAX);
        validate(from, to);
    }

    public static EffectivePeriod from(LocalDateTime from, LocalDateTime to) {
        return EffectivePeriod.builder().from(from).to(to).build();
    }

    public static EffectivePeriod openAt(LocalDateTime at) {
        return EffectivePeriod.builder().from(at).build();
    }

    public static EffectivePeriod mergeRange(List<EffectivePeriod> periodList) {
        var sorted = periodList.stream().sorted(Comparator.comparing(EffectivePeriod::from)).toList();
        return from(sorted.getFirst().from(), sorted.getLast().to());
    }

    public static void validate(LocalDateTime from, LocalDateTime to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' is must be after than 'from'");
        }
    }

    public EffectivePeriod copyAfterAt(LocalDateTime at) {
        return toBuilder().from(at).to(to).build();
    }

    public EffectivePeriod copyBeforeAt(LocalDateTime at) {
        return toBuilder().from(from).to(at).build();
    }

    public boolean isOverlap(EffectivePeriod other) {
        return from.isBefore(other.to()) && to.isAfter(other.from());
    }

    public boolean contains(LocalDateTime time) {
        return from.isBefore(time) && to.isAfter(time);
    }

    public boolean isOpen() {
        return to.isEqual(MAX);
    }
}