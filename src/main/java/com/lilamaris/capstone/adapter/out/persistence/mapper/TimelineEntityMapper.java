package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.SnapshotLink;
import com.lilamaris.capstone.domain.timeline.Timeline;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TimelineEntityMapper {
    public static Timeline toDomain(TimelineEntity entity) {
        var id = Timeline.Id.from(entity.getId());
        var snapshotMap = entity.getSnapshotList()
                .stream()
                .map(SnapshotEntityMapper::toDomain)
                .collect(Collectors.toMap(Snapshot::id, Function.identity()));
        var snapshotLinkMap = entity.getSnapshotLinkList().stream()
                .map(SnapshotLinkEntityMapper::toDomain)
                .collect(Collectors.toMap(SnapshotLink::id, Function.identity()));

        return Timeline.from(id, entity.getDescription(), snapshotMap, snapshotLinkMap);
    }

    public static TimelineEntity toEntity(Timeline domain) {
        var snapshotList = domain.snapshotMap().values().stream().map(SnapshotEntityMapper::toEntity).toList();
        var snapshotLinkList = domain.snapshotLinkMap().values().stream()
                .map(SnapshotLinkEntityMapper::toEntity)
                .toList();

        return TimelineEntity.builder()
                .id(domain.id().value())
                .description(domain.description())
                .snapshotList(snapshotList)
                .snapshotLinkList(snapshotLinkList)
                .build();
    }
}

