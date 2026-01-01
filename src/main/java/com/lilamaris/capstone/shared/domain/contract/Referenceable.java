package com.lilamaris.capstone.shared.domain.contract;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

public interface Referenceable {
    DomainRef ref();

    default boolean refersTo(DomainRef other) {
        return ref().equals(other);
    }
}
