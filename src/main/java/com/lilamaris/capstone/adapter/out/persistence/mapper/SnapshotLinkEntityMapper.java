package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotLinkEntity;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.SnapshotLink;
import com.lilamaris.capstone.domain.timeline.Timeline;

import java.util.Optional;

public class SnapshotLinkEntityMapper {
    public static SnapshotLink toDomain(SnapshotLinkEntity entity) {
        var id = SnapshotLink.Id.from(entity.getId());
        var descendantSnapshotId = Snapshot.Id.from(entity.getDescendantSnapshotId());
        var timelineId = Timeline.Id.from(entity.getTimelineId());
        var ancestorSnapshotId = Snapshot.Id.from(entity.getAncestorSnapshotId());
        var domainDeltaList = entity.getDomainDeltaList().stream().map(DomainDeltaEntityMapper::toDomain).toList();
        return SnapshotLink.from(id, timelineId, descendantSnapshotId, ancestorSnapshotId, domainDeltaList);
    }

    public static SnapshotLinkEntity toEntity(SnapshotLink domain) {
        var domainDeltaList = domain.domainDeltaList().stream().map(DomainDeltaEntityMapper::toEntity).toList();
        var ancestorSnapshotId = Optional.ofNullable(domain.ancestorSnapshotId()).map(Snapshot.Id::value).orElse(null);
        return SnapshotLinkEntity.builder()
                .id(domain.id().value())
                .descendantSnapshotId(domain.descendantSnapshotId().value())
                .timelineId(domain.timelineId().value())
                .ancestorSnapshotId(ancestorSnapshotId)
                .domainDeltaList(domainDeltaList)
                .build();
    }
}
