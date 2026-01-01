package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;

public record DefaultDescriptionMetadata(
        String title,
        String details
) implements DescriptionMetadata {
}
