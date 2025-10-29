package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    public Timeline copyWithSnapshotListDiff(List<Snapshot> needUpsert) {
        var part = needUpsert
                .stream()
                .collect(Collectors.partitioningBy(s -> s.id() != null));
        Map<Snapshot.Id, Snapshot> needUpdate = part.get(true).stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));
        List<Snapshot> needInsert = part.get(false);

        var updated = new ArrayList<>(
                snapshotList
                        .stream()
                        .map(s -> Optional.ofNullable(needUpdate.remove(s.id())).orElse(s))
                        .toList()
        );
        updated.addAll(needInsert);

        return copyWithSnapshotList(updated);
    }

    private List<Snapshot> getSnapshotValidAtRange(LocalDateTime validFrom, LocalDateTime validTo, Predicate<Snapshot> predicate) {
        var range = EffectivePeriod.from(validFrom, validTo);
        return snapshotList
                .stream()
                .filter(s -> s.valid().isOverlap(range))
                .filter(predicate)
                .toList();
    }

    private List<Snapshot> getSnapshotValidAt(LocalDateTime validAt, Predicate<Snapshot> predicate) {
        return snapshotList
                .stream()
                .filter(s -> s.valid().contains(validAt))
                .filter(predicate)
                .toList();
    }

    public Timeline migrate(LocalDateTime txAt, LocalDateTime validAt, String description) {
        if (snapshotList.isEmpty()) {
            return copyWithSnapshotList(List.of(Snapshot.create(id, txAt, validAt, description)));
        }

        var sourceList = getSnapshotValidAt(validAt, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        Snapshot source = sourceList.getFirst();
        EffectivePeriod.validate(source.valid().from(), validAt);

        var upgradeTransition = source.upgrade(txAt, description);
        var migrateTransition = upgradeTransition.next().migrate(validAt, description);

        var needUpsert = Stream.of(upgradeTransition.prev(), migrateTransition.prev(), migrateTransition.next()).toList();
        return copyWithSnapshotListDiff(needUpsert);
    }

    public Timeline merge(LocalDateTime txAt, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        if (snapshotList.isEmpty()) {
            throw new IllegalStateException("No snapshot exists in the timeline.");
        }

        var sourceList = getSnapshotValidAtRange(validFrom, validTo, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        var closedSource = sourceList.stream()
                .map(s -> s.copyWithTx(s.tx().copyBeforeAt(txAt)))
                .toList();

        var mergedValid = EffectivePeriod.mergeRange(sourceList.stream().map(Snapshot::valid).toList());
        var mergedSnapshot = Snapshot.create(id, EffectivePeriod.openAt(txAt), mergedValid, description);

        var needUpsert = Stream.concat(closedSource.stream(), Stream.of(mergedSnapshot)).toList();
        return copyWithSnapshotListDiff(needUpsert);
    }

    public List<Snapshot> rollback(LocalDateTime txAt, LocalDateTime targetTxAt) {
        if (snapshotList.isEmpty()) {
            throw new IllegalStateException("No snapshot exists in the timeline.");
        }

        var source = snapshotList.stream().collect(
                Collectors.teeing(
                        Collectors.filtering(s -> s.tx().contains(targetTxAt), Collectors.toList()),
                        Collectors.filtering(s -> s.tx().isOpen(), Collectors.toList()),
                        (containing, open) -> Map.of(
                                "containing", containing,
                                "open", open
                        )
                )
        );
        List<Snapshot> containingTxAt = source.get("containing");
        List<Snapshot> openTx = source.get("open");

        var closedSnapshots = openTx.stream().map(s -> s.closeTxAt(txAt)).toList();
        var rollbackSnapshot = containingTxAt.stream().map(s -> s.copyWithTx(EffectivePeriod.openAt(txAt))).toList();

        return Stream.concat(closedSnapshots.stream(), rollbackSnapshot.stream()).toList();
    }
}
