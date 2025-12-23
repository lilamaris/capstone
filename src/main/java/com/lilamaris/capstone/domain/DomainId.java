package com.lilamaris.capstone.domain;

public interface DomainId<ID, D extends DomainType> {
    ID getValue();

    D getDomainType();

    String asString();
}
