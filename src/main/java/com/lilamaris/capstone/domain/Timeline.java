package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Timeline(
        Id id,
        String description,
        List<Snapshot> snapshotList
) implements BaseDomain<Timeline.Id, Timeline> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    @Builder
    public record SnapshotTransition(
            Id timelineId,
            List<Snapshot> before,
            List<Snapshot> after
    ) implements Transition<Snapshot> {
        public SnapshotTransition {
            before = Optional.ofNullable(before).orElse(new ArrayList<>());
            after = Optional.ofNullable(after).orElse(new ArrayList<>());
        }

        @Override
        public Function<Snapshot, Map<String, Object>> fieldExtractor() {
            return (s) -> Map.of(
                    "description", s.description(),
                    "tx.from", s.tx().from(),
                    "tx.to", s.tx().to(),
                    "valid.from", s.valid().from(),
                    "valid.to", s.valid().to()
            );
        }

        @Override
        public BiFunction<Snapshot, Snapshot, TransitionType> lifeCycleStrategy() {
            return (before, after) -> {
                if (after == null)
                    throw new IllegalStateException("Invalid lifecycle");

                if (!after.tx().isOpen()) return TransitionType.RETIRE;
                if (after.id() == null) return TransitionType.CREATE;
                else return TransitionType.UPDATE;
            };
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

    public Timeline applyTransition(SnapshotTransition transition) {
        if (!transition.timelineId().equals(id)) {
            throw new IllegalStateException("Snapshots managed on other timelines cannot be reflect: The timeline IDs for the transitions are different.");
        }
        var exists = snapshotList.stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));
        if (!transition.before.stream().allMatch(s -> exists.get(s.id()) != null)) {
            throw new IllegalStateException("Snapshots that do not exist on the timeline cannot be modified: The snapshot in the ‘before’ state is not present in the snapshot list for that timeline.");
        }

        var reflect = transition.after.stream().collect(Collectors.partitioningBy(s -> s.id() != null));
        var needUpdate = reflect.get(true).stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));
        var needInsert = reflect.get(false);

        List<Snapshot> updated = snapshotList.stream().map(s -> needUpdate.getOrDefault(s.id(), s)).collect(Collectors.toList());
        updated.addAll(needInsert);

        return toBuilder().snapshotList(updated).build();
    }

    public SnapshotTransition migratePreview(LocalDateTime txAt, LocalDateTime validAt, String description) {
        var va = EffectiveConvertible.of(validAt);
        var ta = EffectiveConvertible.of(txAt);

        if (snapshotList.isEmpty()) {
            return SnapshotTransition.builder().timelineId(id).after(List.of(Snapshot.create(id, ta, va, description))).build();
        }

        var sourceList = getSnapshotValidAt(validAt, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        Snapshot source = sourceList.getFirst();
        Effective.validate(source.valid().from(), validAt);

        var closedSource = source.closeTxAt(txAt);

        var splitPeriod = source.valid().splitAt(validAt);
        var migratedLeft = Snapshot.create(id, ta, splitPeriod.left(), description);
        var migratedRight = Snapshot.create(id, ta, splitPeriod.right(), description);

        List<Snapshot> before = List.of(source);
        List<Snapshot> after = List.of(closedSource, migratedLeft, migratedRight);

        return SnapshotTransition.builder().timelineId(id).before(before).after(after).build();
    }

    public SnapshotTransition mergePreview(LocalDateTime txAt, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        var ta = EffectiveConvertible.of(txAt);

        if (snapshotList.isEmpty()) {
            throw new IllegalStateException("No snapshot exists in the timeline.");
        }

        var sourceList = getSnapshotValidAtRange(validFrom, validTo, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        var closedSource = sourceList.stream()
                .map(s -> s.closeTxAt(txAt))
                .toList();

        var mergedValid = Effective.mergeRange(sourceList.stream().map(Snapshot::valid).toList());
        var mergedSnapshot = Snapshot.create(id, ta, mergedValid, description);

        List<Snapshot> before = sourceList.stream().toList();
        List<Snapshot> after = Stream.concat(closedSource.stream(), Stream.of(mergedSnapshot)).toList();

        return SnapshotTransition.builder().timelineId(id).before(before).after(after).build();
    }

    public SnapshotTransition rollback(LocalDateTime txAt, LocalDateTime targetTxAt) {
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
        var rollbackSnapshot = containingTxAt.stream().map(s -> s.copyWithTx(Effective.openAt(txAt))).toList();

        List<Snapshot> before = openTx.stream().toList();
        List<Snapshot> after = Stream.concat(closedSnapshots.stream(), rollbackSnapshot.stream()).toList();

        return SnapshotTransition.builder().timelineId(id).before(before).after(after).build();
    }

    private List<Snapshot> getSnapshotValidAtRange(LocalDateTime validFrom, LocalDateTime validTo, Predicate<Snapshot> predicate) {
        var range = Effective.from(validFrom, validTo);
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
}
