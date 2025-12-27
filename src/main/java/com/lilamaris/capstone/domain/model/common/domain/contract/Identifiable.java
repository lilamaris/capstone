package com.lilamaris.capstone.domain.model.common.domain.contract;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

public interface Identifiable<ID extends DomainId<?>> {
    ID id();
}
