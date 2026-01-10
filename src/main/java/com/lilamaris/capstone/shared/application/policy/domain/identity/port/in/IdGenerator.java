package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;

public interface IdGenerator<T extends DomainId<?>> {
    Class<T> supports();

    T next();
}
