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

    public List<Snapshot> migrate(Snapshot source, LocalDateTime txAt, LocalDateTime validAt) {
        if (validAt.isBefore(source.valid().from())) {
            throw new IllegalArgumentException("the valid period of the new snapshot must be later than most recent snapshot's one.");
        }

        var upgradeTransition = source.upgrade(txAt);
        var migrateTransition = upgradeTransition.next().migrate(validAt);

        return List.of(upgradeTransition.prev(), migrateTransition.prev(), migrateTransition.next());
    }

    public List<Snapshot> merge(List<Snapshot> snapshotList, LocalDateTime txAt) {
        if (!snapshotList.stream().allMatch(snapshot -> snapshot.tx().isOpen())) {
            throw new IllegalStateException("all snapshots for the merge operation must have their tx is open.");
        }

        var snapshotsSortedByValid = snapshotList.stream().sorted(Comparator.comparing(s -> s.valid().from())).toList();
        var mergeSnapshotVersionNo = snapshotList.stream()
                .max(Comparator.comparing(Snapshot::versionNo))
                .map(Snapshot::versionNo)
                .orElseThrow(NoSuchElementException::new);

        var mostOldest = snapshotsSortedByValid.getFirst().valid().from();
        var mostLatest = snapshotsSortedByValid.getLast().valid().to();

        var txClosedSnapshots = snapshotList.stream().map(
                snapshot -> snapshot.copyWithTx(snapshot.tx().copyBeforeAt(txAt), snapshot.versionNo())
        ).toList();

        var mergedSnapshotTx = EffectivePeriod.openAt(txAt);
        var mergedSnapshotValid = EffectivePeriod.from(mostOldest, mostLatest);
        var mergedSnapshot = Snapshot.create(id, mergedSnapshotTx, mergedSnapshotValid, mergeSnapshotVersionNo + 1);

        return Stream.concat(txClosedSnapshots.stream(), Stream.of(mergedSnapshot)).toList();
    }
}
