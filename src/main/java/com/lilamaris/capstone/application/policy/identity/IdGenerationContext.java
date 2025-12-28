package com.lilamaris.capstone.application.policy.identity;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

public interface IdGenerationContext {
    <T extends DomainId<R>, R> T next(Class<T> spec);
}
