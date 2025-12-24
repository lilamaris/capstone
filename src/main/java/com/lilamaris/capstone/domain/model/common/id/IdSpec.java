package com.lilamaris.capstone.domain.model.common.id;

public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
