package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.BaseAuditableEntity;
import com.lilamaris.capstone.domain.embed.Audit;

public class AuditableEntityMapper {
    public static Audit toDomain(BaseAuditableEntity<?> entity) {
        return Audit.from(entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
