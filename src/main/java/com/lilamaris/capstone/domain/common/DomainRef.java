package com.lilamaris.capstone.domain.common;

public interface DomainRef {
    DomainType type();
    DomainId<?> id();
}
