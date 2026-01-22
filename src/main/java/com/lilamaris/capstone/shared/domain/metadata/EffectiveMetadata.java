package com.lilamaris.capstone.shared.domain.metadata;

import java.time.Instant;

public interface EffectiveMetadata {
    Instant from();

    Instant to();

    Instant MAX = Instant.parse("9999-12-31T23:59:59Z");

    default Instant max() {
        return MAX;
    }

    default boolean isOpen() {
        return to().equals(max());
    };

    default boolean isOverlap(EffectiveMetadata other) {
        return from().isBefore(other.to()) && other.from().isBefore(to());
    }

    default boolean contains(Instant time) {
        return !time.isBefore(from()) && time.isBefore(to());
    }
}
