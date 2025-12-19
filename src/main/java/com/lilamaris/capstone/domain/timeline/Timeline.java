package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.embed.*;
import com.lilamaris.capstone.domain.exception.DomainInvariantException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineDomainException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineErrorCode;
import lombok.Builder;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Timeline(
        Id id,
        String description,
        Map<Snapshot.Id, Snapshot> snapshotMap,
        Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap,
        Map<Snapshot.Id, SnapshotLink> snapshotLinkByAncestor,
        Map<Snapshot.Id, SnapshotLink> snapshotLinkByDescendant,
        Audit audit
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "timeline";
        }
    }
    
    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id() {
            super();
        }

        public Id(UUID uuid) {
            super(uuid);
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }

    public Timeline {
        id = Optional.ofNullable(id).orElseGet(Id::new);
        description = Optional.ofNullable(description).orElse("No description");
        snapshotMap = Optional.ofNullable(snapshotMap).orElse(new HashMap<>());
        snapshotLinkMap = Optional.ofNullable(snapshotLinkMap).orElse(new HashMap<>());
        snapshotLinkByAncestor = snapshotLinkMap.values().stream()
                .filter(l -> l.ancestorSnapshotId() != null)
                .collect(Collectors.toMap(SnapshotLink::ancestorSnapshotId, Function.identity()));
        snapshotLinkByDescendant = snapshotLinkMap.values().stream()
                .collect(Collectors.toMap(SnapshotLink::descendantSnapshotId, Function.identity()));
    }

    public static Timeline from(Id id, String description, Map<Snapshot.Id, Snapshot> snapshotMap, Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap, Audit audit) {
        return getDefaultBuilder()
                .id(id)
                .description(description)
                .snapshotMap(snapshotMap)
                .snapshotLinkMap(snapshotLinkMap)
                .audit(audit)
                .build();
    }

    public static Timeline create(String description) {
        return getDefaultBuilder().description(description).build();
    }

    public Timeline copyWithDescription(String description) {
        return toBuilder().description(description).build();
    }

    public List<Snapshot> getSnapshotTxAt(Instant txAt) {
        return snapshotMap.values().stream()
                .filter(s -> s.tx().contains(txAt))
                .toList();
    }

    public List<Snapshot> getSnapshotTxAtRange(Effective range) {
        return snapshotMap.values().stream()
                .filter(s -> s.tx().isOverlap(range))
                .toList();
    }

    public List<Snapshot> getSnapshotValidAt(Instant validAt) {
        return snapshotMap.values().stream()
                .filter(s -> s.valid().contains(validAt))
                .toList();
    }

    public List<Snapshot> getSnapshotValidAtRange(Effective range) {
        return snapshotMap.values().stream()
                .filter(s -> s.valid().isOverlap(range))
                .toList();
    }

    public Map<DomainId<?, ?>, List<DomainDelta>> resolveAllDeltaOf(
            Snapshot.Id snapshotId,
            String domainType
    ) {
        Map<DomainId<?, ?>, List<DomainDelta>> bucket = new HashMap<>();
        List<SnapshotLink> descToRoot = buildDescendantToRootChain(snapshotId);

        descToRoot.stream()
                .flatMap(l -> l.domainDeltaList().stream())
                .filter(d -> domainType.equals(d.domainType()))
                .forEach(d -> bucket.computeIfAbsent(d.domainId(), k -> new ArrayList<>()).add(d));

        return bucket;
    }

    public Timeline updateDomainDelta(Snapshot.Id targetSnapshotId, String domainType, DomainId<?, ?> domainId, DomainDelta.Patch patch) {
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);
        var targetSnapshotLink = snapshotLinkByDescendant.get(targetSnapshotId);
        var domainDelta = DomainDelta.create(targetSnapshotLink.id(), domainType, domainId, patch);
        var updatedSnapshotLink = targetSnapshotLink.upsertDomainDelta(List.of(domainDelta));
        currentSnapshotLinkMap.put(updatedSnapshotLink.id(), updatedSnapshotLink);

        return copyWithSnapshotLink(currentSnapshotLinkMap);
    }

    public Timeline migrateSnapshot(Instant txAt, Instant validAt, String description) {
        var currentSnapshotMap = new HashMap<>(snapshotMap);
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);

        if (snapshotMap.isEmpty()) {
            var initialSnapshot = Snapshot.create(Effective.openAt(txAt), Effective.openAt(validAt), id, description);
            var initialSnapshotLink = SnapshotLink.createRoot(id, initialSnapshot.id());

            updateSnapshotMap(currentSnapshotMap, List.of(initialSnapshot));
            updateSnapshotLinkMap(currentSnapshotLinkMap, List.of(initialSnapshotLink));

            return copyWithSnapshotContext(currentSnapshotMap, currentSnapshotLinkMap);
        }

        var sourceList = getSnapshotValidAt(validAt).stream()
                .filter(s -> s.tx().isOpen())
                .sorted(Comparator.comparing(s -> s.valid().from()))
                .toList();

        if (sourceList.isEmpty()) {
            throw new TimelineDomainException(
                    TimelineErrorCode.NO_AVAILABLE_SNAPSHOT,
                    "No snapshots match the given range or criteria."
            );
        }

        Snapshot source = sourceList.getFirst();

        var closedSource = source.closeTxAt(txAt);

        var splitPeriod = source.valid().splitAt(validAt);

        var migratedLeft = Snapshot.create(Effective.openAt(txAt), splitPeriod.left(), id, description);
        var migratedLeftLink = SnapshotLink.create(id, migratedLeft.id(), closedSource.id());

        var migratedRight = Snapshot.create(Effective.openAt(txAt), splitPeriod.right(), id, description);
        var migratedRightLink = SnapshotLink.create(id, migratedRight.id(), migratedLeft.id());

        var updateSnapshot = List.of(closedSource, migratedLeft, migratedRight);
        var updateSnapshotLink = List.of(migratedLeftLink, migratedRightLink);

        updateSnapshotMap(currentSnapshotMap, updateSnapshot);
        updateSnapshotLinkMap(currentSnapshotLinkMap, updateSnapshotLink);

        return copyWithSnapshotContext(currentSnapshotMap, currentSnapshotLinkMap);
    }

    public Timeline mergeSnapshot(Instant txAt, Effective validRange, String description) {
        if (snapshotMap.isEmpty()) {
            throw new TimelineDomainException(
                    TimelineErrorCode.EMPTY_SNAPSHOT,
                    "Timeline contains no snapshots."
            );
        }
        if (snapshotMap.size() != snapshotLinkMap.size()) {
            throw new TimelineDomainException(
                    TimelineErrorCode.SNAPSHOT_LINK_INCONSISTENT,
                    "Snapshot and SnapshotLink counts do not match. Timeline invariant violated."
            );
        }

        var currentSnapshotMap = new HashMap<>(snapshotMap);
        var currentSnapshotLinkMap = new HashMap<>(snapshotLinkMap);

        var sourceList = getSnapshotValidAtRange(validRange).stream()
                .filter(s -> s.tx().isOpen())
                .sorted(Comparator.comparing(s -> s.tx().from()))
                .toList();

        if (sourceList.isEmpty()) {
            throw new TimelineDomainException(
                    TimelineErrorCode.NO_AVAILABLE_SNAPSHOT,
                    "No snapshots match the given range or criteria."
            );
        }

        var closedSource = sourceList.stream()
                .map(s -> s.closeTxAt(txAt))
                .toList();

        var mergedValid = sourceList.getFirst().valid().mergeRange(sourceList.getLast().valid());

        var mergedSnapshot = Snapshot.create(Effective.openAt(txAt), mergedValid, this.id(), description);
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

    private Timeline copyWithSnapshotContext(Map<Snapshot.Id, Snapshot> snapshotMap, Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap) {
        return toBuilder().snapshotMap(snapshotMap).snapshotLinkMap(snapshotLinkMap).build();
    }

    private Timeline copyWithSnapshotLink(Map<SnapshotLink.Id, SnapshotLink> snapshotLinkMap) {
        return toBuilder().snapshotLinkMap(snapshotLinkMap).build();
    }

    private List<SnapshotLink> buildDescendantToRootChain(Snapshot.Id startSnapshotId) {
        List<SnapshotLink> chain = new ArrayList<>();
        Set<Snapshot.Id> visited = new HashSet<>();
        var current = startSnapshotId;

        while (true) {
            if (!visited.add(current)) {
                throw new TimelineDomainException(
                        TimelineErrorCode.SNAPSHOT_LINK_CYCLE,
                        String.format(
                                "SnapshotLink cycle detected: descendant snapshot id %s forms a cyclic reference. Timeline invariant violated.",
                                current
                        )
                );
            }

            var link = snapshotLinkByDescendant.get(current);

            if (link == null) {
                throw new TimelineDomainException(
                        TimelineErrorCode.SNAPSHOT_LINK_INCONSISTENT,
                        String.format(
                                "Inconsistent SnapshotLink structure: no SnapshotLink found for descendant snapshot id %s. Timeline invariant violated.",
                                current
                        )
                );
            }

            chain.add(link);

            if (link.ancestorSnapshotId() == null) {
                return chain;
            }

            current = link.ancestorSnapshotId();
        }
    }

    private void updateSnapshotMap(Map<Snapshot.Id, Snapshot> current, List<Snapshot> toUpdate) {
        toUpdate.forEach(s -> {
            if (s.id() == null) {
                throw new DomainInvariantException(
                        "Snapshot ID must not be null. Timeline invariant violated."
                );
            }
            current.put(s.id(), s);
        });
    }

    private void updateSnapshotLinkMap(Map<SnapshotLink.Id, SnapshotLink> current, List<SnapshotLink> toUpdate) {
        toUpdate.forEach(l -> {
            if (l.id() == null) {
                throw new DomainInvariantException(
                        "SnapshotLink ID must not be null. Timeline invariant violated."
                );
            }
            if (l.descendantSnapshotId() == null) {
                throw new DomainInvariantException(
                        "SnapshotLink descendantSnapshotId must not be null. Timeline invariant violated."
                );
            }
            current.put(l.id(), l);
        });
    }
}
