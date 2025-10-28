package com.lilamaris.capstone.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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

    public List<Snapshot> migrate(Snapshot source, LocalDateTime txAt, LocalDateTime validAt, String description) {
        validateMigrate(source, validAt);

        var upgradeTransition = source.upgrade(txAt, description);
        var migrateTransition = upgradeTransition.next().migrate(validAt, description);
        return List.of(upgradeTransition.prev(), migrateTransition.prev(), migrateTransition.next());
    }

    private void validateMigrate(Snapshot source, LocalDateTime validAt) {
        if (validAt.isBefore(source.valid().from())) {
            throw new IllegalArgumentException("The 'validAt' must be later than or equal to the source's validFrom.");
        }
    }

    public List<Snapshot> merge(List<Snapshot> snapshotList, LocalDateTime txAt, String description) {
        validateMerge(snapshotList);

        var closedSnapshots = snapshotList.stream()
                .map(s -> s.copyWithTx(s.tx().copyBeforeAt(txAt), s.versionNo()))
                .toList();

        var newVersionNo = snapshotList.stream()
                .map(Snapshot::versionNo)
                .max(Integer::compareTo)
                .orElseThrow(NoSuchElementException::new);

        var mergedValid = EffectivePeriod.mergeRange(snapshotList.stream().map(Snapshot::valid).toList());
        var mergedSnapshot = Snapshot.create(id, EffectivePeriod.openAt(txAt), mergedValid, newVersionNo + 1, description);

        return Stream.concat(closedSnapshots.stream(), Stream.of(mergedSnapshot)).toList();
    }

    private void validateMerge(List<Snapshot> snapshotList) {
        if (snapshotList.isEmpty()) {
            throw new IllegalArgumentException("No snapshots provided for merge.");
        }
        if (!snapshotList.stream().allMatch(s -> s.tx().isOpen())) {
            throw new IllegalStateException("All snapshots to be merged must have an open transaction period.");
        }
    }
}
