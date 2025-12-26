package com.lilamaris.capstone.domain.model.common.embed.impl;

import com.lilamaris.capstone.domain.model.common.embed.DescriptionMetadata;

public record DefaultDescriptionMetadata(
        String title,
        String details
) implements DescriptionMetadata {
}
