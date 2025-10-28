package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class TimelineEntityMapper {
    public static Timeline toDomain(TimelineEntity entity) {
        var id = Timeline.Id.from(entity.getId());
        var snapshotIdList = entity.getSnapshotList()
                .stream()
                .map(SnapshotEntity::getId)
                .map(Snapshot.Id::from)
                .toList();

        return Timeline.builder()
                .id(id)
                .description(entity.getDescription())
                .snapshotIdList(snapshotIdList)
                .build();
    }

    public static TimelineEntity toEntity(Timeline domain, EntityManager em) {
        var id = Optional.ofNullable(domain.id()).map(Timeline.Id::value).orElse(null);

        return TimelineEntity.builder()
                .id(id)
                .description(domain.description())
                .build();
    }


}

