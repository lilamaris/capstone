package com.lilamaris.capstone.domain.model.common.domain.id;

public interface IdGenerationContext {
    <T extends DomainId<R>, R> T next(Class<T> spec);
}
