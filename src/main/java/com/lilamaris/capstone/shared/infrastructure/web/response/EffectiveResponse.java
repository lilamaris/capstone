package com.lilamaris.capstone.shared.infrastructure.web.response;

import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;

import java.time.Instant;

public record EffectiveResponse(
        Instant from,
        Instant to
) {
    public static EffectiveResponse from(EffectiveMetadata effectiveMetadata) {
        return new EffectiveResponse(
                effectiveMetadata.from(),
                effectiveMetadata.to()
        );
    }
}
