package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.EffectiveEmbeddableEntity;
import com.lilamaris.capstone.domain.embed.Effective;

public class EffectiveEmbeddableEntityMapper {
    public static Effective toDomain(EffectiveEmbeddableEntity entity) {
        return Effective.from(entity.getFrom(), entity.getTo());
    }

    public static EffectiveEmbeddableEntity toEntity(Effective domain) {
        return EffectiveEmbeddableEntity.builder()
                .from(domain.from())
                .to(domain.to())
                .build();
    }
}
