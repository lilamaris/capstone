package com.lilamaris.capstone.shared.domain.contract;

import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;

public interface Describable {
    DescriptionMetadata descriptionMetadata();

    void updateDescription(DescriptionMetadata descriptionMetadata);
}
