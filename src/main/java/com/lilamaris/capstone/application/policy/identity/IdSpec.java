package com.lilamaris.capstone.application.policy.identity;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

@FunctionalInterface
public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
