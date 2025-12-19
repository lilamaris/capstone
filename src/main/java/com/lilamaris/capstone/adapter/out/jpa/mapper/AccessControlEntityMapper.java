package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.AccessControlEntity;
import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.user.User;

public class AccessControlEntityMapper {
    public static AccessControl toDomain(AccessControlEntity entity) {
        var id = new AccessControl.Id(entity.getId());
        var userId = new User.Id(entity.getUserId());
        var resource = DomainRefEmbeddableEntityMapper.toDomain(entity.getResource());
        var scopedRole = entity.getScopedRole();
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return new AccessControl(id, userId, resource, scopedRole, audit);
    }

    public static AccessControlEntity toEntity(AccessControl domain) {
        var id = domain.id().getValue();
        var userId = domain.userId().getValue();
        var resource = DomainRefEmbeddableEntityMapper.toEntity(domain.resource());
        var scopedRole = domain.scopedRole();
        return AccessControlEntity.builder()
                .id(id)
                .userId(userId)
                .resource(resource)
                .scopedRole(scopedRole)
                .build();
    }
}
