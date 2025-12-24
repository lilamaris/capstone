package com.lilamaris.capstone.domain.model.common.mixin;

import com.lilamaris.capstone.domain.model.common.id.DomainId;

public interface Identifiable<ID extends DomainId<?>> {
    ID id();
}
