package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.BaseEntity;
import com.lilamaris.capstone.domain.embed.Audit;

public class AuditEmbeddableEntityMapper {
    public static Audit toDomain(BaseEntity<?> entity) {
        return Audit.from(entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
