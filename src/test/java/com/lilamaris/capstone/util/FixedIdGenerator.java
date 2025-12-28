package com.lilamaris.capstone.util;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

public class FixedIdGenerator<T extends DomainId<?>> implements IdGenerator<T> {
    private final Class<T> type;
    private final T fixed;

    public FixedIdGenerator(Class<T> type, T fixed) {
        this.type = type;
        this.fixed = fixed;
    }

    @Override
    public T next() {
        return fixed;
    }

    @Override
    public Class<T> supports() {
        return type;
    }
}
