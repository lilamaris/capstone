package com.lilamaris.capstone.domain.model.common.id;

@FunctionalInterface
public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
