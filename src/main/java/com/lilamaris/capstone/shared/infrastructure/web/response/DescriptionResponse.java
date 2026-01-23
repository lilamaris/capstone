package com.lilamaris.capstone.shared.infrastructure.web.response;

import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import org.springframework.lang.Nullable;

public record DescriptionResponse(
        String title,
        @Nullable String details
) {
    public static DescriptionResponse from(DescriptionMetadata description) {
        return new DescriptionResponse(
                description.title(),
                description.details()
        );
    }
}
