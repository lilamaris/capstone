package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;

import java.time.Instant;

public record DefaultEffectiveMetadata(
        Instant from,
        Instant to
) implements EffectiveMetadata {
    public static DefaultEffectiveMetadata from(EffectiveMetadata effectiveMetadata) {
        return new DefaultEffectiveMetadata(effectiveMetadata.from(), effectiveMetadata.to());
    }
}
