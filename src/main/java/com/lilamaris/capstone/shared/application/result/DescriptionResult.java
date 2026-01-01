package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.domain.contract.Describable;

public record DescriptionResult(String title, String details) {
    public static DescriptionResult from(Describable domain) {
        var description = domain.descriptionMetadata();
        return new DescriptionResult(description.title(), description.details());
    }
}
