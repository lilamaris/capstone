package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
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
) {
    public SnapshotLink {
        if (timelineId == null) throw new DomainIllegalArgumentException("Field 'timelineId' must not be null.");
        if (descendantSnapshotId == null)
            throw new DomainIllegalArgumentException("Field 'descendantSnapshotId' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::new);
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

    private static SnapshotLinkBuilder getDefaultBuilder(Timeline.Id timelineId, Snapshot.Id descendantSnapshotId) {
        return builder().timelineId(timelineId).descendantSnapshotId(descendantSnapshotId);
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

    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "timeline.snapshot-link";
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
}
