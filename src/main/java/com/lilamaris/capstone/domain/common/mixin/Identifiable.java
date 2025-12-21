package com.lilamaris.capstone.domain.common.mixin;

import com.lilamaris.capstone.domain.common.DomainId;

public interface Identifiable<ID extends DomainId<?>> {
    ID id();
}
