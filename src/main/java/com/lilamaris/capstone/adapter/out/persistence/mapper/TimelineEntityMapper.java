package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static TimelineEntity toEntity(Timeline domain, EntityManager em) {
        TimelineEntity entity = Optional.ofNullable(domain.id())
                .map(Timeline.Id::value)
                .map(id -> em.find(TimelineEntity.class, id))
                .orElseGet(() -> TimelineEntity.builder().build());

        updateEntity(entity, domain);
        return entity;
    }

    public static void updateEntity(TimelineEntity entity, Timeline domain) {
        entity.setDescription(domain.description());

        Map<UUID, SnapshotEntity> exist = entity.getSnapshotList()
                .stream()
                .filter(e -> e.getId() != null)
                .collect(Collectors.toMap(SnapshotEntity::getId, Function.identity()));

        List<SnapshotEntity> needUpdate = new ArrayList<>();

        for (var s : domain.snapshotList()) {
            var se = Optional.ofNullable(s.id()).map(Snapshot.Id::value).map(exist::remove).orElse(null);
            if (se == null) {
                se = SnapshotEntityMapper.toEntity(s, entity);
            } else {
                SnapshotEntityMapper.updateEntity(se, s);
                if (se.getTimeline() != entity) {
                    se.setTimeline(entity);
                }
            }
            needUpdate.add(se);
        }
        entity.getSnapshotList().clear();
        entity.getSnapshotList().addAll(needUpdate);
    }
}

