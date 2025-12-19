package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.DomainDeltaEntity;
import com.lilamaris.capstone.domain.timeline.DomainDelta;
import com.lilamaris.capstone.domain.timeline.SnapshotLink;

import java.util.UUID;

public class DomainDeltaEntityMapper {
    public static DomainDelta toDomain(DomainDeltaEntity entity) {
        var id = new DomainDelta.Id(entity.getId());
        var snapshotLinkId = new SnapshotLink.Id(entity.getSnapshotLinkId());
        var patch = new DomainDelta.JsonPatch(entity.getPatch());
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

//        return DomainDelta.from(id, snapshotLinkId, entity.getDomainType(), domainId, patch, audit);
        return null;
    }

    public static DomainDeltaEntity toEntity(DomainDelta domain) {
        return DomainDeltaEntity.builder()
                .id(domain.id().getValue())
                .snapshotLinkId(domain.snapshotLinkId().getValue())
                .domainType(domain.domainType())
//                .domainId(domain.domainId().value().toString())
                .patch(domain.patch().getRaw())
                .build();
    }
}
