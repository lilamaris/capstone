package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.DomainRefEmbeddableEntity;
import com.lilamaris.capstone.domain.embed.DomainRef;

public class DomainRefEmbeddableEntityMapper {
    public static DomainRef toDomain(DomainRefEmbeddableEntity entity) {
        return new DomainRef(entity.getId(), entity.getType());
    }

    public static DomainRefEmbeddableEntity toEntity(DomainRef domain) {
        return DomainRefEmbeddableEntity.builder()
                .id(domain.id())
                .type(domain.type())
                .build();
    }
}
