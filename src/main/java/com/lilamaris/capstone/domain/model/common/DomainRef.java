package com.lilamaris.capstone.domain.model.common;

public interface DomainRef {
    DomainType type();
    DomainId<?> id();
}
