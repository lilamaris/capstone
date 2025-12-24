package com.lilamaris.capstone.domain.model.common.id;

public interface IdGenerator {
    <T extends DomainId<?>, R> T generate(IdSpec<T, R> spec, RawGenerator<R> rawGenerator);
}
