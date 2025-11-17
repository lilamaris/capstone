package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Audit;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record SnapshotLink(
        Id id,
        Timeline.Id timelineId,
        Snapshot.Id ancestorSnapshotId,
        Snapshot.Id descendantSnapshotId,
        Boolean isRoot,
        List<DomainDelta> domainDeltaList,
        Audit audit
) implements BaseDomain<SnapshotLink.Id, SnapshotLink> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public SnapshotLink {
        id = Optional.ofNullable(id).orElseGet(Id::random);

        timelineId = Optional.ofNullable(timelineId).orElseThrow(() -> new IllegalArgumentException("'timelineId' of type Timeline.Id cannot be null"));
        descendantSnapshotId = Optional.ofNullable(descendantSnapshotId).orElseThrow(() -> new IllegalArgumentException("'descendantSnapshotId' of type Snapshot.Id cannot be null"));

        isRoot = ancestorSnapshotId == null;
        domainDeltaList = Optional.ofNullable(domainDeltaList).orElseGet(ArrayList::new);
    }

    public static SnapshotLink from(Id id, Timeline.Id timelineId, Snapshot.Id descendantSnapshotId, Snapshot.Id ancestorSnapshotId, List<DomainDelta> domainDeltaList, Audit audit) {
        return getDefaultBuilder(timelineId, descendantSnapshotId)
                .id(id)
                .ancestorSnapshotId(ancestorSnapshotId)
                .domainDeltaList(domainDeltaList)
                .audit(audit)
                .build();
    }

    public static SnapshotLink createRoot(Timeline.Id timelineId, Snapshot.Id rootSnapshotId) {
        return getDefaultBuilder(timelineId, rootSnapshotId).build();
    }

    public static SnapshotLink create(Timeline.Id timelineId, Snapshot.Id descendantSnapshotId, Snapshot.Id ancestorSnapshotId) {
        return getDefaultBuilder(timelineId, descendantSnapshotId)
                .ancestorSnapshotId(ancestorSnapshotId)
                .build();
    }

    public static SnapshotLink create(Timeline.Id timelineId, Snapshot.Id descendantSnapshotId, Snapshot.Id ancestorSnapshotId, List<DomainDelta> domainDeltaList) {
        return getDefaultBuilder(timelineId, descendantSnapshotId)
                .ancestorSnapshotId(ancestorSnapshotId)
                .domainDeltaList(domainDeltaList)
                .build();
    }

    public SnapshotLink copyWithDomainDelta(List<DomainDelta> domainDeltaList) {
        return toBuilder().domainDeltaList(domainDeltaList).build();
    }

    public SnapshotLink upsertDomainDelta(List<DomainDelta> domainDeltaList) {
        var upsertMap = domainDeltaList.stream().collect(Collectors.toMap(DomainDelta::id, Function.identity()));
        var updated = this.domainDeltaList.stream()
                .map(d -> {
                    var id = d.id();
                    if (upsertMap.containsKey(id)) {
                        var newValue = upsertMap.get(id);
                        upsertMap.remove(id);
                        return newValue;
                    }
                    return d;
                })
                .collect(Collectors.toList());
        updated.addAll(upsertMap.values());

        return copyWithDomainDelta(updated);
    }

    private static SnapshotLinkBuilder getDefaultBuilder(Timeline.Id timelineId, Snapshot.Id descendantSnapshotId) {
        return builder().timelineId(timelineId).descendantSnapshotId(descendantSnapshotId);
    }
}
