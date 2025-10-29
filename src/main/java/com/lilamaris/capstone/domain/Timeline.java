package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Timeline(
        Id id,
        String description,
        List<Snapshot> snapshotList
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
        snapshotList = Optional.ofNullable(snapshotList).orElse(new ArrayList<>());
    }

    public static Timeline initial(String description) {
        return builder().description(description).build();
    }

    public static Timeline from(Timeline.Id id, String description) {
        return builder().id(id).description(description).build();
    }

    public Timeline copyWithSnapshotList(List<Snapshot> snapshotList) {
        return toBuilder().snapshotList(snapshotList).build();
    }

    private List<Snapshot> getSnapshotTxAt(LocalDateTime txAt) {
        return snapshotList
                .stream()
                .filter(s -> s.isOpenTxAt(txAt))
                .sorted(Comparator.comparing(s -> s.valid().from()))
                .toList();
    }

    private List<Snapshot> getSnapshotValidAt(LocalDateTime validAt) {
        return snapshotList
                .stream()
                .filter(s -> s.isOpenValidAt(validAt))
                .sorted(Comparator.comparing(s -> s.tx().from()))
                .toList();
    }

    public List<Snapshot> migrate(LocalDateTime txAt, LocalDateTime validAt, String description) {
        var validList = getSnapshotValidAt(validAt);
        if (validList.isEmpty()) {
            return List.of(Snapshot.initial(id, txAt, validAt, description));
        }

        Snapshot source = validList.getLast();
        EffectivePeriod.validate(source.valid().from(), validAt);

        var upgradeTransition = source.upgrade(txAt, description);
        var migrateTransition = upgradeTransition.next().migrate(validAt, description);
        return List.of(upgradeTransition.prev(), migrateTransition.prev(), migrateTransition.next());
    }

    public List<Snapshot> merge(List<Snapshot> targetSnapshotList, LocalDateTime txAt, String description) {
        validateAllTransactionState(targetSnapshotList, true);

        var closedSnapshots = targetSnapshotList.stream()
                .map(s -> s.copyWithTx(s.tx().copyBeforeAt(txAt)))
                .toList();

        var newVersionNo = targetSnapshotList.stream()
                .map(Snapshot::versionNo)
                .max(Integer::compareTo)
                .orElseThrow(NoSuchElementException::new);

        var mergedValid = EffectivePeriod.mergeRange(targetSnapshotList.stream().map(Snapshot::valid).toList());
        var mergedSnapshot = Snapshot.create(id, EffectivePeriod.openAt(txAt), mergedValid, newVersionNo + 1, description);

        return Stream.concat(closedSnapshots.stream(), Stream.of(mergedSnapshot)).toList();
    }

    public List<Snapshot> rollback(List<Snapshot> targetSnapshotList, List<Snapshot> recentSnapshotList, LocalDateTime txAt) {
        validateAllTransactionState(targetSnapshotList, false);
        validateAllTransactionState(recentSnapshotList, true);

        var closedSnapshots = recentSnapshotList.stream().map(s -> s.copyWithTx(s.tx().copyBeforeAt(txAt))).toList();
        var rollbackSnapshot = targetSnapshotList.stream().map(s -> s.copyWithTx(EffectivePeriod.openAt(txAt))).toList();

        return Stream.concat(closedSnapshots.stream(), rollbackSnapshot.stream()).toList();
    }

    private void validateAllTransactionState(List<Snapshot> snapshotList, boolean except) {
        if (snapshotList.isEmpty()) {
            throw new IllegalArgumentException("No snapshots provided for merge.");
        }
        if (!snapshotList.stream().allMatch(s -> s.tx().isOpen() == except)) {
           throw new IllegalStateException("Snapshots do not meet the transaction state requirement");
        }
    }
}
