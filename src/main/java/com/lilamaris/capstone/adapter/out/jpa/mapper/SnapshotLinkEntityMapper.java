package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.SnapshotLinkEntity;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.SnapshotLink;
import com.lilamaris.capstone.domain.timeline.Timeline;

import java.util.Optional;

public class SnapshotLinkEntityMapper {
    public static SnapshotLink toDomain(SnapshotLinkEntity entity) {
        var id = new SnapshotLink.Id(entity.getId());
        var descendantSnapshotId = new Snapshot.Id(entity.getDescendantSnapshotId());
        var timelineId = new Timeline.Id(entity.getTimelineId());
        var ancestorSnapshotId = entity.getAncestorSnapshotId() != null ? new Snapshot.Id(entity.getAncestorSnapshotId()) : null;
        var domainDeltaList = entity.getDomainDeltaList().stream().map(DomainDeltaEntityMapper::toDomain).toList();
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return SnapshotLink.from(id, timelineId, descendantSnapshotId, ancestorSnapshotId, domainDeltaList, audit);
    }

    public static SnapshotLinkEntity toEntity(SnapshotLink domain) {
        var domainDeltaList = domain.domainDeltaList().stream().map(DomainDeltaEntityMapper::toEntity).toList();
        var ancestorSnapshotId = Optional.ofNullable(domain.ancestorSnapshotId()).map(Snapshot.Id::getValue).orElse(null);
        return SnapshotLinkEntity.builder()
                .id(domain.id().getValue())
                .descendantSnapshotId(domain.descendantSnapshotId().getValue())
                .timelineId(domain.timelineId().getValue())
                .ancestorSnapshotId(ancestorSnapshotId)
                .domainDeltaList(domainDeltaList)
                .build();
    }
}
