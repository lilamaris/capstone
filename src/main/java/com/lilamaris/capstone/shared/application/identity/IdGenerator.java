package com.lilamaris.capstone.shared.application.identity;

import com.lilamaris.capstone.shared.domain.id.DomainId;

public interface IdGenerator<T extends DomainId<?>> {
    Class<T> supports();

    T next();
}
