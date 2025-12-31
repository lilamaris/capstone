package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.model.common.domain.contract.Describable;

public record DescriptionResult(String title, String details) {
    public static DescriptionResult from(Describable domain) {
        var description = domain.descriptionMetadata();
        return new DescriptionResult(description.title(), description.details());
    }
}
