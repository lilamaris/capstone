package com.lilamaris.capstone.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Builder(toBuilder = true)
public record Snapshot(
        Id id,
        EffectivePeriod tx,
        EffectivePeriod valid,
        Integer versionNo,
        String description,
        Timeline.Id timelineId
) {
    public static final String NAME = "Snapshot";

    public record Id(UUID value) {
        public static Id random() {
            return new Id(UUID.randomUUID());
        }

        public static Id from(UUID value) {
            return new Id(value);
        }
    }

    @Builder
    public record Transition(Snapshot prev, Snapshot next) {}

    public Snapshot {
        tx = Optional.ofNullable(tx).orElseThrow(() -> new IllegalArgumentException("'tx' must be set"));
        valid = Optional.ofNullable(valid).orElseThrow(() -> new IllegalArgumentException("'valid' must be set"));
        timelineId = Optional.ofNullable(timelineId).orElseThrow(() -> new IllegalArgumentException("'timelineId' must be set"));
        versionNo = Optional.ofNullable(versionNo).orElse(1);
        description = Optional.ofNullable(description).orElse("No user description.");
    }

    public static Snapshot initial(Timeline.Id timelineId, LocalDateTime validAt, LocalDateTime now, String description) {
        var tx = EffectivePeriod.openAt(now);
        var valid = EffectivePeriod.openAt(validAt);

        return Snapshot.builder()
                .timelineId(timelineId)
                .valid(valid)
                .tx(tx)
                .description(description)
                .build();
    }

    public Transition migrate(LocalDateTime validAt) {
        validateOperation();
        var prev = toBuilder()
                .valid(valid.copyBeforeAt(validAt))
                .build();

        var next = toBuilder()
                .id(null)
                .valid(valid.copyAfterAt(validAt))
                .build();

        return new Transition(prev, next);
    }

    public Transition upgrade(LocalDateTime txAt) {
        validateOperation();

        var prev = toBuilder()
                .tx(tx.copyBeforeAt(txAt))
                .build();

        var next = toBuilder()
                .id(null)
                .tx(tx.copyAfterAt(txAt))
                .versionNo(versionNo + 1)
                .build();
        return new Transition(prev, next);
    }

    public Snapshot copy() {
        return toBuilder().build();
    }

    private void validateOperation() {
        if (!tx.isOpen()) {
            throw new IllegalStateException("can only operated to snapshots with open tx");
        }
    }
}
