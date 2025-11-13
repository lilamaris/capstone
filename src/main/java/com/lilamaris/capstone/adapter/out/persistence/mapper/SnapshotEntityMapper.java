package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.DomainDeltaEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SnapshotEntityMapper {
    public static Snapshot toDomain(SnapshotEntity entity) {
        var id = Snapshot.Id.from(entity.getId());
        var tx = Effective.from(entity.getTxFrom(), entity.getTxTo());
        var valid = Effective.from(entity.getValidFrom(), entity.getValidTo());
        var timelineId = Timeline.Id.from(entity.getTimelineId());
        var baseSnapshotId = Snapshot.Id.from(entity.getBaseSnapshotId());
        var domainDeltaEntityList = entity.getDomainDeltaEntityList().stream().map(DomainDeltaEntityMapper::toDomain).toList();

        return Snapshot.builder()
                .id(id)
                .tx(tx)
                .valid(valid)
                .versionNo(entity.getVersionNo())
                .description(entity.getDescription())
                .timelineId(timelineId)
                .baseSnapshotId(baseSnapshotId)
                .deltaList(domainDeltaEntityList)
                .build();
    }

    public static SnapshotEntity toEntity(Snapshot domain) {
        var baseSnapshotId = Optional.ofNullable(domain.baseSnapshotId()).map(Snapshot.Id::value).orElse(null);
        var domainDeltaEntityList = domain.deltaList().stream().map(DomainDeltaEntityMapper::toEntity).toList();
        return SnapshotEntity.builder()
                .id(domain.id().value())
                .txFrom(domain.tx().from())
                .txTo(domain.tx().to())
                .validFrom(domain.valid().from())
                .validTo(domain.valid().to())
                .versionNo(domain.versionNo())
                .description(domain.description())
                .timelineId(domain.timelineId().value())
                .baseSnapshotId(baseSnapshotId)
                .domainDeltaEntityList(domainDeltaEntityList)
                .build();
    }
}
