package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.OrganizationEntity;
import com.lilamaris.capstone.domain.degree.Organization;
import com.lilamaris.capstone.domain.timeline.Snapshot;

public class OrganizationEntityMapper {
    public static Organization toDomain(OrganizationEntity entity) {
        var id = Organization.Id.from(entity.getId());
        var parentId = Organization.Id.from(entity.getParentId());
        var groupId = Organization.GroupId.from(entity.getGroupId());
        var snapshotId = Snapshot.Id.from(entity.getSnapshotId());

        return Organization.builder()
                .id(id)
                .parentId(parentId)
                .groupId(groupId)
                .snapshotId(snapshotId)
                .build();
    }

    public static OrganizationEntity toEntity(Organization domain) {
        return OrganizationEntity.builder()
                .id(domain.id().value())
                .parentId(domain.parentId().value())
                .groupId(domain.groupId().value())
                .snapshotId(domain.snapshotId().value())
                .build();
    }
}
