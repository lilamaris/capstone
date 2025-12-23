package com.lilamaris.capstone.domain.model.common.mixin;

import com.lilamaris.capstone.domain.model.common.DomainRef;

public interface Referenceable {
    DomainRef ref();
    default boolean refersTo(DomainRef other) {
        return ref().equals(other);
    }
}
