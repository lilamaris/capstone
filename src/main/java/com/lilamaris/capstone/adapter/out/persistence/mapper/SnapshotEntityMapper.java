package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;

public class SnapshotEntityMapper {
    public static Snapshot toDomain(SnapshotEntity entity) {
        var id = Snapshot.Id.from(entity.getId());
        var tx = Effective.from(entity.getTxFrom(), entity.getTxTo());
        var valid = Effective.from(entity.getValidFrom(), entity.getValidTo());
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
                .txFrom(domain.tx().from())
                .txTo(domain.tx().to())
                .validFrom(domain.valid().from())
                .validTo(domain.valid().to())
                .versionNo(domain.versionNo())
                .description(domain.description())
                .timelineId(domain.timelineId().value())
                .build();
    }
}
