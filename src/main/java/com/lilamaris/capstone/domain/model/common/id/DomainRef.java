package com.lilamaris.capstone.domain.model.common.id;

import com.lilamaris.capstone.domain.model.common.type.DomainType;

public interface DomainRef {
    DomainType type();

    String id();
}
