package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmbeddableTemporalRange implements TemporalRange {
    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    private EmbeddableTemporalRange(Instant startAt, Instant endAt) {
        TemporalRange.validate(startAt, endAt);

        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static EmbeddableTemporalRange of(Instant startAt, Instant endAt) {
        return new EmbeddableTemporalRange(startAt, endAt);
    }

    public static EmbeddableTemporalRange of(Instant startAt, Duration duration) {
        Preconditions.requireNonNull(startAt, "startAt");
        Preconditions.requirePositive(duration, "duration");

        var endAt = startAt.plus(duration);
        return of(startAt, endAt);
    }

    public static EmbeddableTemporalRange from(TemporalRange range) {
        Preconditions.requireNonNull(range, "range");

        return of(range.startAt(), range.endAt());
    }

    @Override
    public Instant startAt() {
        return startAt;
    }

    @Override
    public Instant endAt() {
        return endAt;
    }

    public void updateStartAt(Instant startAt) {
        apply(startAt, endAt);
    }

    public void updateEndAt(Instant endAt) {
        apply(startAt, endAt);
    }

    public void extendStartAt(Duration extension) {
        Preconditions.requireNonNull(extension, "extension");

        apply(startAt.plus(extension), endAt);
    }

    public void extendEndAt(Duration extension) {
        Preconditions.requireNonNull(extension, "extension");

        apply(startAt, endAt.plus(extension));
    }

    public void adjustOffset(Duration offset) {
        Preconditions.requireNonNull(offset, "offset");

        apply(startAt.plus(offset), endAt.plus(offset));
    }

    private void apply(Instant startAt, Instant endAt) {
        Preconditions.requireNonNull(startAt, "startAt");
        Preconditions.requireNonNull(endAt, "endAt");
        TemporalRange.validate(startAt, endAt);

        this.startAt = startAt;
        this.endAt = endAt;
    }
}
