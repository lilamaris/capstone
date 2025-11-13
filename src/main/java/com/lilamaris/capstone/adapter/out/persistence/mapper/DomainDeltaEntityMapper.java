package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.DomainDeltaEntity;
import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.timeline.DomainDelta;
import com.lilamaris.capstone.domain.timeline.Snapshot;

import java.util.UUID;

public class DomainDeltaEntityMapper {
    public static DomainDelta toDomain(DomainDeltaEntity entity) {
        var id = DomainDelta.Id.from(entity.getId());
        var snapshotId = Snapshot.Id.from(entity.getSnapshotId());
        var domainId = BaseDomain.UuidId.from(UUID.fromString(entity.getDomainId()));

        return DomainDelta.builder()
                .id(id)
                .snapshotId(snapshotId)
                .domainName(entity.getDomainName())
                .domainId(domainId)
                .patch(new DomainDelta.JsonPatch(entity.getPatch()))
                .build();
    }

    public static DomainDeltaEntity toEntity(DomainDelta domain) {
        return DomainDeltaEntity.builder()
                .id(domain.id().value())
                .snapshotId(domain.snapshotId().value())
                .domainName(domain.getDomainName())
                .domainId(domain.domainId().value().toString())
                .patch(domain.patch().getRaw())
                .build();
    }
}
