package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Snapshot(
        Id id,
        Effective tx,
        Effective valid,
        Integer versionNo,
        String description,
        Timeline.Id timelineId
) {
    public static final String NAME = "Snapshot";

    public record Id(UUID value) implements DomainId {
        @Override public String getDomainName() { return "Snapshot"; }
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
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

    public static Snapshot create(Timeline.Id timelineId, EffectiveConvertible tx, EffectiveConvertible valid, String description) {
        return Snapshot.builder()
                .timelineId(timelineId)
                .tx(tx.convert())
                .valid(valid.convert())
                .description(description)
                .build();
    }

    public void validateOperation() {
        if (!tx.isOpen()) {
            throw new IllegalStateException("can only operated to snapshots with open tx");
        }
    }

    public Snapshot copyWithTx(Effective tx) {
        return toBuilder().tx(tx).versionNo(versionNo + 1).build();
    }

    public Snapshot copyWithValid(Effective valid) {
        return toBuilder().valid(valid).versionNo(versionNo + 1).build();
    }

    public Snapshot closeTxAt(LocalDateTime at) {
        return copyWithTx(tx.copyBeforeAt(at));
    }

    public Snapshot closeValidAt(LocalDateTime at) {
        return copyWithValid(valid.copyBeforeAt(at));
    }

    public Transition migrate(LocalDateTime validAt, String description) {
        validateOperation();
        var prev = copyWithValid(valid.copyBeforeAt(validAt))
                .toBuilder()
                .description(description)
                .versionNo(1)
                .build();
        var next = copyWithValid(valid.copyAfterAt(validAt))
                .toBuilder()
                .id(null)
                .description(description)
                .versionNo(1)
                .build();
        return new Transition(prev, next);
    }

    public Transition upgrade(LocalDateTime txAt, String description) {
        validateOperation();
        var prev = copyWithTx(tx.copyBeforeAt(txAt));
        var next = copyWithTx(tx.copyAfterAt(txAt))
                .toBuilder()
                .id(null)
                .description(description)
                .versionNo(1)
                .build();
        return new Transition(prev, next);
    }
}