package com.lilamaris.capstone.domain.model.common.id;

public interface IdGenerationContext {
    <T extends DomainId<R>, R> T next(IdSpec<T, R> spec);
}
