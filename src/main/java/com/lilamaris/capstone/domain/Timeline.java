package com.lilamaris.capstone.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Timeline(
        Id id,
        String description,
        List<Snapshot.Id> snapshotIdList
) {
    public record Id(UUID value) {
        public static Id random() {
            return new Id(UUID.randomUUID());
        }

        public static Id from(UUID value) {
            return new Id(value);
        }
    }

    public Timeline {
        snapshotIdList = Optional.ofNullable(snapshotIdList).orElse(List.of());
    }

    public static Timeline initial(String description) {
        return builder().description(description).build();
    }

    public static Timeline from(Timeline.Id id, String description) {
        return builder().id(id).description(description).build();
    }

    public List<Snapshot> migrate(Snapshot source, LocalDateTime validAt) {
        if (validAt.isBefore(source.valid().from())) {
            throw new IllegalArgumentException("the valid period of the new snapshot must be later than most recent snapshot's one.");
        }

        var txAt = EffectivePeriod.now();
        var upgradeTransition = source.upgrade(txAt);
        var migrateTransition = upgradeTransition.next().migrate(validAt);

        return List.of(upgradeTransition.prev(), migrateTransition.prev(), migrateTransition.next());
    }
}
