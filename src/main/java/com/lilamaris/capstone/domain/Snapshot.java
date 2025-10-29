package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    public static Snapshot create(Timeline.Id timelineId, EffectivePeriod tx, EffectivePeriod valid, Integer versionNo, String description) {
        return Snapshot.builder()
                .timelineId(timelineId)
                .tx(tx)
                .valid(valid)
                .versionNo(versionNo)
                .description(description)
                .build();
    }

    public static Snapshot create(Timeline.Id timelineId, EffectivePeriod tx, EffectivePeriod valid, Integer versionNo) {
        return create(timelineId, tx, valid, versionNo, null);
    }

    public static Snapshot initial(Timeline.Id timelineId, LocalDateTime txAt, LocalDateTime validAt, String description) {
        var tx = EffectivePeriod.openAt(txAt);
        var valid = EffectivePeriod.openAt(validAt);
        return create(timelineId, tx, valid, 1, description);
    }

    public void validateOperation() {
        if (!tx.isOpen()) {
            throw new IllegalStateException("can only operated to snapshots with open tx");
        }
    }

    public Snapshot copyWithTx(EffectivePeriod tx) {
        return toBuilder().tx(tx).versionNo(versionNo + 1).build();
    }

    public Snapshot copyWithValid(EffectivePeriod valid) {
        return toBuilder().valid(valid).versionNo(versionNo + 1).build();
    }

    public Transition migrate(LocalDateTime validAt, String description) {
        validateOperation();
        var prev = copyWithValid(valid.copyBeforeAt(validAt));
        var next = copyWithValid(valid.copyAfterAt(validAt)).toBuilder().id(null).description(description).build();
        return new Transition(prev, next);
    }

    public Transition upgrade(LocalDateTime txAt, String description) {
        validateOperation();
        var prev = copyWithTx(tx.copyBeforeAt(txAt));
        var next = copyWithTx(tx.copyAfterAt(txAt)).toBuilder().id(null).description(description).build();
        return new Transition(prev, next);
    }

    public boolean isOpenValidAt(LocalDateTime validAt) {
        return valid.contains(validAt);
    }

    public boolean isOpenTxAt(LocalDateTime txAt) {
        return tx.contains(txAt);
    }
}
