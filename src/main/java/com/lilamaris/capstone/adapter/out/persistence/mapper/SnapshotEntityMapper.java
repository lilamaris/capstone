package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.Effective;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SnapshotEntityMapper {
    public static Snapshot toDomain(SnapshotEntity entity) {
        var id = Snapshot.Id.from(entity.getId());
        var tx = Effective.from(entity.getTxFrom(), entity.getTxTo());
        var valid = Effective.from(entity.getValidFrom(), entity.getValidTo());
        var timelineId = Timeline.Id.from(entity.getTimeline().getId());

        return Snapshot.builder()
                .id(id)
                .tx(tx)
                .valid(valid)
                .versionNo(entity.getVersionNo())
                .description(entity.getDescription())
                .timelineId(timelineId)
                .build();
    }

    public static SnapshotEntity toEntity(Snapshot domain, EntityManager em) {
        var id = Optional.ofNullable(domain.id()).map(Snapshot.Id::value).orElse(null);
        var timeline = em.getReference(TimelineEntity.class, domain.timelineId().value());

        return SnapshotEntity.builder()
                .id(id)
                .txFrom(domain.tx().from())
                .txTo(domain.tx().to())
                .validFrom(domain.valid().from())
                .validTo(domain.valid().to())
                .versionNo(domain.versionNo())
                .description(domain.description())
                .timeline(timeline)
                .build();
    }

    public static SnapshotEntity toEntity(Snapshot domain, TimelineEntity parent) {
        var id = Optional.ofNullable(domain.id()).map(Snapshot.Id::value).orElse(null);

        return SnapshotEntity.builder()
                .id(id)
                .txFrom(domain.tx().from())
                .txTo(domain.tx().to())
                .validFrom(domain.valid().from())
                .validTo(domain.valid().to())
                .versionNo(domain.versionNo())
                .description(domain.description())
                .timeline(parent)
                .build();
    }

    public static void updateEntity(SnapshotEntity entity, Snapshot domain) {
        entity.setTxFrom(domain.tx().from());
        entity.setTxTo(domain.tx().to());
        entity.setValidFrom(domain.valid().from());
        entity.setValidTo(domain.valid().to());
        entity.setVersionNo(domain.versionNo());
        entity.setDescription(domain.description());
    }
}
