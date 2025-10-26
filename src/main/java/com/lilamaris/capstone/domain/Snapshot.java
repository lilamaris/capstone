package com.lilamaris.capstone.domain;

import lombok.*;

import java.text.MessageFormat;
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
    public static final String INITIAL_MESSAGE = "Initialize at {0} [V{1}]";
    public static final String UPGRADE_MESSAGE = "Tx {0} at {1}";
    public static final String MIGRATE_MESSAGE = "Valid {0} at {1}";

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
        var now = EffectivePeriod.now();

        tx = Optional.ofNullable(tx).orElse(EffectivePeriod.openAt(now));
        valid = Optional.ofNullable(valid).orElse(EffectivePeriod.openAt(now));
        versionNo = Optional.ofNullable(versionNo).orElse(1);
        timelineId = Optional.ofNullable(timelineId).orElseThrow(() -> new IllegalArgumentException("'timelineId' must be set"));

        var initialDescription = MessageFormat.format(INITIAL_MESSAGE, now, versionNo);
        description = String.join(
                "\n",
                initialDescription,
                Optional.ofNullable(description).orElse("")
        );
    }

    public static Snapshot initial(Timeline.Id timelineId) {
        return Snapshot.builder().timelineId(timelineId).build();
    }

    public Transition migrate() {
        var now = EffectivePeriod.now();
        var prevDescription = String.join(
                "\n",
                description,
                MessageFormat.format(MIGRATE_MESSAGE, "closed", now)
        );
        var nextDescription = MessageFormat.format(MIGRATE_MESSAGE, "opened", now);

        var prev = toBuilder()
                .valid(valid.closeAt(now))
                .description(prevDescription)
                .build();

        var next = toBuilder()
                .valid(EffectivePeriod.openAt(now))
                .versionNo(versionNo + 1)
                .description(nextDescription)
                .build();

        return new Transition(prev, next);
    }

    public Transition upgrade() {
        var now = EffectivePeriod.now();
        var prevDescription = String.join(
                "\n",
                description,
                MessageFormat.format(UPGRADE_MESSAGE, "closed", now)
        );
        var nextDescription = MessageFormat.format(UPGRADE_MESSAGE, "opened", now);

        var prev = toBuilder()
                .tx(tx.closeAt(now))
                .description(prevDescription)
                .build();

        var next = toBuilder()
                .tx(EffectivePeriod.openAt(now))
                .versionNo(versionNo + 1)
                .description(nextDescription)
                .build();
        return new Transition(prev, next);
    }
}
