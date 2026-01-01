package com.lilamaris.capstone.shared.domain.contract;

import com.lilamaris.capstone.shared.domain.id.DomainId;

public interface Identifiable<ID extends DomainId<?>> {
    ID id();
}
