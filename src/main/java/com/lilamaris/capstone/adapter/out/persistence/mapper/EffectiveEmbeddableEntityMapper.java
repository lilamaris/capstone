package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.EffectiveEmbeddableEntity;
import com.lilamaris.capstone.application.configuration.ApplicationContext;
import com.lilamaris.capstone.domain.embed.Effective;

import java.time.ZoneId;

public class EffectiveEmbeddableEntityMapper {
    public static Effective toDomain(EffectiveEmbeddableEntity entity) {
        var systemZone = ApplicationContext.getSystemZone();
        var storedZone = ZoneId.of(entity.getZoneId());

        if (!storedZone.equals(systemZone)) {
            throw new IllegalStateException("EffectiveEmbeddableEntity zone mismatch: entity zone="
                    + storedZone + " does not match system zone=" + systemZone);
        }

        var from = entity.getFrom();
        var to = entity.getTo();

        boolean isMax = to.toInstant().equals(Effective.MAX.toInstant());

        if (!isMax) {
            var fromZone = from.getZone();
            var toZone = to.getZone();

            if (!systemZone.equals(fromZone)) {
                throw new IllegalStateException("EffectiveEmbeddableEntity zone mismatch: entity zone="
                        + fromZone + ", system zone=" + systemZone);
            }

            if (!fromZone.equals(toZone)) {
                throw new IllegalStateException("EffectiveEmbeddableEntity from/to zone mismatch: "
                        + fromZone + " vs " + toZone);
            }
        }

        return Effective.from(from, to);
    }

    public static EffectiveEmbeddableEntity toEntity(Effective domain) {
        var systemZone = ApplicationContext.getSystemZone();
        return EffectiveEmbeddableEntity.builder()
                .from(domain.from())
                .to(domain.to())
                .zoneId(systemZone.getId())
                .build();
    }
}
