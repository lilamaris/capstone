package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.DomainDeltaEntity;
import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.timeline.DomainDelta;
import com.lilamaris.capstone.domain.timeline.SnapshotLink;

import java.util.UUID;

public class DomainDeltaEntityMapper {
    public static DomainDelta toDomain(DomainDeltaEntity entity) {
        var id = DomainDelta.Id.from(entity.getId());
        var snapshotLinkId = SnapshotLink.Id.from(entity.getSnapshotLinkId());
        var domainId = BaseDomain.UuidId.from(UUID.fromString(entity.getDomainId()));

        return DomainDelta.builder()
                .id(id)
                .snapshotLinkId(snapshotLinkId)
                .domainType(entity.getDomainType())
                .domainId(domainId)
                .patch(new DomainDelta.JsonPatch(entity.getPatch()))
                .build();
    }

    public static DomainDeltaEntity toEntity(DomainDelta domain) {
        return DomainDeltaEntity.builder()
                .id(domain.id().value())
                .snapshotLinkId(domain.snapshotLinkId().value())
                .domainType(domain.domainType())
                .domainId(domain.domainId().value().toString())
                .patch(domain.patch().getRaw())
                .build();
    }
}
