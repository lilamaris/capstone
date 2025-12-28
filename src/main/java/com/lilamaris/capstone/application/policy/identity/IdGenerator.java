package com.lilamaris.capstone.application.policy.identity;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

public interface IdGenerator<T extends DomainId<?>> {
    Class<T> supports();

    T next();
}
