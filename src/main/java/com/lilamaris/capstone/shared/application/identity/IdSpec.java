package com.lilamaris.capstone.shared.application.identity;

import com.lilamaris.capstone.shared.domain.id.DomainId;

@FunctionalInterface
public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
