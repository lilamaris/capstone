package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.model.common.embed.DescriptionMetadata;

public record DescriptionResult(String title, String details) {
    public static DescriptionResult from(DescriptionMetadata metadata) {
        return new DescriptionResult(metadata.title(), metadata.details());
    }
}
