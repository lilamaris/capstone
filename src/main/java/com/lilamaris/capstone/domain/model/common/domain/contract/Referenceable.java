package com.lilamaris.capstone.domain.model.common.domain.contract;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

public interface Referenceable {
    DomainRef ref();

    default boolean refersTo(DomainRef other) {
        return ref().equals(other);
    }
}
