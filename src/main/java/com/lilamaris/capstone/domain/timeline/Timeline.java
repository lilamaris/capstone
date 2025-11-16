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
        Map<Snapshot.Id, Snapshot> snapshotMap,
        Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap,
        Map<Snapshot.Id, SnapshotLink> snapshotLinkByAncestor,
        Map<Snapshot.Id, SnapshotLink> snapshotLinkByDescendant
) implements BaseDomain<Timeline.Id, Timeline> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Timeline {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        description = Optional.ofNullable(description).orElse("No description");
        snapshotMap = Optional.ofNullable(snapshotMap).orElse(new HashMap<>());
        snapshotLinkMap = Optional.ofNullable(snapshotLinkMap).orElse(new HashMap<>());
        snapshotLinkByAncestor = snapshotLinkMap.values().stream()
                .filter(l -> l.ancestorSnapshotId() != null)
                .collect(Collectors.toMap(SnapshotLink::ancestorSnapshotId, Function.identity()));
        snapshotLinkByDescendant = snapshotLinkMap.values().stream()
                .collect(Collectors.toMap(SnapshotLink::descendantSnapshotId, Function.identity()));
    }

    public static Timeline from(Timeline.Id id, String description, Map<Snapshot.Id, Snapshot> snapshotMap, Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap) {
        return getDefaultBuilder()
                .id(id)
                .description(description)
                .snapshotMap(snapshotMap)
                .snapshotLinkMap(snapshotLinkMap)
                .build();
    }

    public static Timeline create(String description) {
        return getDefaultBuilder().description(description).build();
    }

    public Timeline copyWithDescription(String description) {
        return toBuilder().description(description).build();
    }

    public <T extends BaseDomain<? ,?>> Map<BaseDomain.Id<?>, List<DomainDelta>> resolveAllDeltaOf(
            Class<T> clazz,
            Snapshot.Id snapshotId,
            DomainTypeRegistry registry
    ) {
        String domainType = registry.nameOf(clazz);
        Map<BaseDomain.Id<?>, List<DomainDelta>> bucket = new HashMap<>();
        List<SnapshotLink> descToRoot = buildDescendantToRootChain(snapshotId);

        descToRoot.stream()
                .flatMap(l -> l.domainDeltaList().stream())
                .filter(d -> domainType.equals(d.domainType()))
                .forEach(d -> bucket.computeIfAbsent(d.domainId(), k -> new ArrayList<>()).add(d));

        return bucket;
    }

    public Timeline updateDomainDelta(Snapshot.Id targetSnapshotId, String domainType, BaseDomain.Id<?> domainId, DomainDelta.Patch patch) {
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);
        var targetSnapshotLink = snapshotLinkByDescendant.get(targetSnapshotId);
        var domainDelta = DomainDelta.create(targetSnapshotLink.id(), domainType, domainId, patch);
        var updatedSnapshotLink = targetSnapshotLink.upsertDomainDelta(List.of(domainDelta));
        currentSnapshotLinkMap.put(updatedSnapshotLink.id(), updatedSnapshotLink);

        return copyWithSnapshotLink(currentSnapshotLinkMap);
    }

    public Timeline migrateSnapshot(LocalDateTime txAt, LocalDateTime validAt, String description) {
        var ta = EffectiveConvertible.of(txAt);
        var va = EffectiveConvertible.of(validAt);
        var currentSnapshotMap = new HashMap<>(snapshotMap);
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);

        if (snapshotMap.isEmpty()) {
            var initialSnapshot = Snapshot.create(ta, va, this.id(), description);
            var initialSnapshotLink = SnapshotLink.createRoot(this.id(), initialSnapshot.id());

            updateSnapshotMap(currentSnapshotMap, List.of(initialSnapshot));
            updateSnapshotLinkMap(currentSnapshotLinkMap, List.of(initialSnapshotLink));

            return copyWithSnapshotContext(currentSnapshotMap, currentSnapshotLinkMap);
        }

        var sourceList = getSnapshotValidAt(validAt, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        Snapshot source = sourceList.getFirst();
        Effective.validate(source.valid().from(), validAt);

        var closedSource = source.closeTxAt(txAt);

        var splitPeriod = source.valid().splitAt(validAt);

        var migratedLeft = Snapshot.create(ta, splitPeriod.left(), this.id(), description);
        var migratedLeftLink = SnapshotLink.create(this.id(), migratedLeft.id(), closedSource.id());

        var migratedRight = Snapshot.create(ta, splitPeriod.right(), this.id(), description);
        var migratedRightLink = SnapshotLink.create(this.id(), migratedRight.id(), migratedLeft.id());

        var updateSnapshot = List.of(closedSource, migratedLeft, migratedRight);
        var updateSnapshotLink = List.of(migratedLeftLink, migratedRightLink);

        updateSnapshotMap(currentSnapshotMap, updateSnapshot);
        updateSnapshotLinkMap(currentSnapshotLinkMap, updateSnapshotLink);

        return copyWithSnapshotContext(currentSnapshotMap, currentSnapshotLinkMap);
    }

    public Timeline mergeSnapshot(LocalDateTime txAt, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        var ta = EffectiveConvertible.of(txAt);
        var currentSnapshotMap = new HashMap<>(snapshotMap);
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);

        var sourceList = getSnapshotValidAtRange(validFrom, validTo, s -> s.tx().isOpen());
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("No snapshot exists at that time.");
        }

        var closedSource = sourceList.stream()
                .map(s -> s.closeTxAt(txAt))
                .toList();

        var mergedValid = Effective.mergeRange(sourceList.stream().map(Snapshot::valid).toList());

        var mergedSnapshot = Snapshot.create(ta, mergedValid, this.id(), description);
        var mergedSnapshotLink = SnapshotLink.create(this.id(), mergedSnapshot.id(), closedSource.getLast().id());

        var updateSnapshotMap = Stream.concat(closedSource.stream(), Stream.of(mergedSnapshot)).toList();
        var updateSnapshotLinkMap = List.of(mergedSnapshotLink);

        updateSnapshotMap(currentSnapshotMap, updateSnapshotMap);
        updateSnapshotLinkMap(currentSnapshotLinkMap, updateSnapshotLinkMap);

        return copyWithSnapshotContext(currentSnapshotMap, currentSnapshotLinkMap);
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
//              Map<Snapshot.Id, Snapshot> snapshotMap          (containing, open) -> Map.of(
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

    private static TimelineBuilder getDefaultBuilder() {
        return builder();
    }

    private List<Snapshot> getSnapshotValidAt(LocalDateTime validAt, Predicate<Snapshot> predicate) {
        return snapshotMap.values()
                .stream()
                .filter(s -> s.valid().contains(validAt))
                .filter(predicate)
                .sorted(Comparator.comparing(s -> s.valid().from()))
                .toList();
    }

    private List<Snapshot> getSnapshotValidAtRange(LocalDateTime validFrom, LocalDateTime validTo, Predicate<Snapshot> predicate) {
        var range = Effective.from(validFrom, validTo);
        return snapshotMap.values()
                .stream()
                .filter(s -> s.valid().isOverlap(range))
                .filter(predicate)
                .sorted(Comparator.comparing(s -> s.tx().from()))
                .toList();
    }

    private Timeline copyWithSnapshotContext(Map<Snapshot.Id, Snapshot> snapshotMap, Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap) {
        return toBuilder().snapshotMap(snapshotMap).snapshotLinkMap(snapshotLinkMap).build();
    }

    private Timeline copyWithSnapshotLink(Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap) {
        return toBuilder().snapshotLinkMap(snapshotLinkMap).build();
    }

    private List<SnapshotLink> buildDescendantToRootChain(Snapshot.Id startSnapshotId) {
        return Stream.iterate(
                snapshotLinkByDescendant.get(startSnapshotId),
                Objects::isNull,
                link -> snapshotLinkByDescendant.get(link.ancestorSnapshotId())
        ).toList();
    }

    private void updateSnapshotMap(Map<Snapshot.Id, Snapshot> current, List<Snapshot> toUpdate) {
        toUpdate.forEach(s -> {
            Objects.requireNonNull(s.id(), "Snapshot Id cannot be null");
            current.put(s.id(), s);
        });
    }

    private void updateSnapshotLinkMap(Map<SnapshotLink.Id, SnapshotLink> current, List<SnapshotLink> toUpdate) {
        toUpdate.forEach(l -> {
            Objects.requireNonNull(l.id(), "SnapshotLink Id cannot be null");
            Objects.requireNonNull(l.descendantSnapshotId(), "SnapshotLink descendant snapshot id cannot be null");
            current.put(l.id(), l);
        });
    }
}
