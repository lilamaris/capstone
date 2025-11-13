package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.configuration.DomainTypeRegistry;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.embed.EffectiveConvertible;
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
        List<Snapshot> snapshotList,
        Map<Snapshot.Id, Snapshot> snapshotMap
) implements BaseDomain<Timeline.Id, Timeline> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Timeline {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        snapshotList = Optional.ofNullable(snapshotList).orElse(new ArrayList<>());
        snapshotMap = snapshotList.stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));
    }

    public static Timeline initial(String description) {
        return builder().description(description).build();
    }

    public static Timeline from(Timeline.Id id, String description) {
        return builder().id(id).description(description).build();
    }

    public Timeline migrateSnapshot(LocalDateTime txAt, LocalDateTime validAt, String description) {
        var va = EffectiveConvertible.of(validAt);
        var ta = EffectiveConvertible.of(txAt);

        if (snapshotList.isEmpty()) {
            var updated = upsertSnapshotList(List.of(Snapshot.create(id, null, ta, va, description)));
            return copyWithSnapshotList(updated);
        }

        var sourceList = getSnapshotValidAt(validAt, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        Snapshot source = sourceList.getFirst();
        Effective.validate(source.valid().from(), validAt);

        var closedSource = source.closeTxAt(txAt);

        var splitPeriod = source.valid().splitAt(validAt);
        var migratedLeft = Snapshot.create(id, closedSource.id(), ta, splitPeriod.left(), description);
        var migratedRight = Snapshot.create(id, migratedLeft.id(), ta, splitPeriod.right(), description);

        var updated = upsertSnapshotList(List.of(closedSource, migratedLeft, migratedRight));

        return copyWithSnapshotList(updated);
    }

    public Timeline mergeSnapshot(LocalDateTime txAt, LocalDateTime validFrom, LocalDateTime validTo, String description) {
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
        var mergedSnapshot = Snapshot.create(id, closedSource.getFirst().id(), ta, mergedValid, description);

        var updated = upsertSnapshotList(Stream.concat(closedSource.stream(), Stream.of(mergedSnapshot)).toList());

        return copyWithSnapshotList(updated);
    }

//    public Timeline rollbackSnapshot(LocalDateTime txAt, LocalDateTime targetTxAt) {
//        if (snapshotList.isEmpty()) {
//            throw new IllegalStateException("No snapshot exists in the timeline.");
//        }
//
//        var source = snapshotList.stream().collect(
//                Collectors.teeing(
//                        Collectors.filtering(s -> s.tx().contains(targetTxAt), Collectors.toList()),
//                        Collectors.filtering(s -> s.tx().isOpen(), Collectors.toList()),
//                        (containing, open) -> Map.of(
//                                "containing", containing,
//                                "open", open
//                        )
//                )
//        );
//        List<Snapshot> containingTxAt = source.get("containing");
//        List<Snapshot> openTx = source.get("open");
//
//        var closedSnapshots = openTx.stream().map(s -> s.closeTxAt(txAt)).toList();
//        var rollbackSnapshot = containingTxAt.stream().map(s -> s.copyWithTx(Effective.openAt(txAt))).toList();
//
//        List<Snapshot> before = openTx.stream().toList();
//        List<Snapshot> after = Stream.concat(closedSnapshots.stream(), rollbackSnapshot.stream()).toList();
//
//        return SnapshotTransition.builder().timelineId(id).before(before).after(after).build();
//    }

    public <T extends BaseDomain<? ,?>> Map<BaseDomain.Id<?>, T> resolveAllDeltaOf(
            Class<T> clazz,
            Snapshot.Id snapshotId,
            Map<BaseDomain.Id<?>, T> acc,
            DomainTypeRegistry registry
    ) {
        Snapshot cur = snapshotMap.get(snapshotId);

        // Start downstream -> Collect create delta
        var delta = cur.getDeltaOf(registry.nameOf(clazz)).stream()
                .collect(Collectors.partitioningBy(d -> d.patch().isCreated()));

        Map<BaseDomain.Id<?>, DomainDelta> createDelta = delta.get(true).stream()
                .collect(Collectors.toMap(DomainDelta::domainId, Function.identity()));
        List<DomainDelta> updateDelta = delta.get(false);

        createDelta.forEach((k, v) -> {
            T created = v.patch().convert(clazz);
            acc.putIfAbsent(created.id(), created);
        });
        // End downstream

        // Branch
        Snapshot baseSnapshot = findBaseSnapshotOf(cur);

        if (baseSnapshot == null) {
            updateDelta.forEach(d -> acc.computeIfPresent(d.domainId(), (k, v) -> d.patch().apply(clazz, v)));
            return acc;
        }

        Map<BaseDomain.Id<?>, T> mergedAcc = resolveAllDeltaOf(clazz, baseSnapshot.id(), acc, registry);

        // Start upstream -> Apply update delta
        updateDelta.forEach(d -> mergedAcc.computeIfPresent(d.domainId(), (k, v) -> d.patch().apply(clazz, v)));
        // End upstream

        return mergedAcc;
    }

    private List<Snapshot> upsertSnapshotList(List<Snapshot> snapshots) {
        var upsertMap = snapshots.stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));
        var updated = snapshotList.stream().map(s -> {
            var id = s.id();
            if (upsertMap.containsKey(id)) {
                var newValue = upsertMap.get(id);
                upsertMap.remove(id);
                return newValue;
            }
            return s;
        }).collect(Collectors.toList());
        updated.addAll(upsertMap.values());

        return updated;
    }

    private Timeline copyWithSnapshotList(List<Snapshot> snapshotList) {
        return toBuilder().snapshotList(snapshotList).build();
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

    private Snapshot findBaseSnapshotOf(Snapshot snapshot) {
        var baseSnapshotId = Optional.ofNullable(snapshot).map(Snapshot::baseSnapshotId).orElse(null);
        if (baseSnapshotId == null) return null;
        return snapshotMap.get(baseSnapshotId);
    }
}
