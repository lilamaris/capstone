package com.lilamaris.capstone.domain.model.common.domain.contract;

import com.lilamaris.capstone.domain.model.common.domain.metadata.DescriptionMetadata;

public interface Describable {
    DescriptionMetadata descriptionMetadata();

    void updateDescription(DescriptionMetadata descriptionMetadata);
}
