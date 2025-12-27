package com.lilamaris.capstone.domain.model.common.domain.id;

@FunctionalInterface
public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
