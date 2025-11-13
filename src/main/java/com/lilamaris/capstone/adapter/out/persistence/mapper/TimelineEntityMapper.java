package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.timeline.Timeline;

import java.util.*;

public class TimelineEntityMapper {
    public static Timeline toDomain(TimelineEntity entity) {
        var id = Timeline.Id.from(entity.getId());
        var snapshotList = entity.getSnapshotList()
                .stream()
                .map(SnapshotEntityMapper::toDomain)
                .toList();

        return Timeline.builder()
                .id(id)
                .description(entity.getDescription())
                .snapshotList(snapshotList)
                .build();
    }

    public static TimelineEntity toEntity(Timeline domain) {
        var snapshotList = domain.snapshotList().stream().map(SnapshotEntityMapper::toEntity).toList();
        return TimelineEntity.builder()
                .id(domain.id().value())
                .description(domain.description())
                .snapshotList(snapshotList)
                .build();
    }
}

