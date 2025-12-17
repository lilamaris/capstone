package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.SnapshotEntity;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;

public class SnapshotEntityMapper {
    public static Snapshot toDomain(SnapshotEntity entity) {
        var id = Snapshot.Id.from(entity.getId());
        var tx = EffectiveEmbeddableEntityMapper.toDomain(entity.getTx());
        var valid = EffectiveEmbeddableEntityMapper.toDomain(entity.getValid());
        var timelineId = Timeline.Id.from(entity.getTimelineId());

        return Snapshot.builder()
                .id(id)
                .tx(tx)
                .valid(valid)
                .versionNo(entity.getVersionNo())
                .description(entity.getDescription())
                .timelineId(timelineId)
                .build();
    }

    public static SnapshotEntity toEntity(Snapshot domain) {
        return SnapshotEntity.builder()
                .id(domain.id().value())
                .tx(EffectiveEmbeddableEntityMapper.toEntity(domain.tx()))
                .valid(EffectiveEmbeddableEntityMapper.toEntity(domain.valid()))
                .versionNo(domain.versionNo())
                .description(domain.description())
                .timelineId(domain.timelineId().value())
                .build();
    }
}
