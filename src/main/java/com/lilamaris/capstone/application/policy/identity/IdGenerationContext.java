package com.lilamaris.capstone.application.policy.identity;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;

import java.util.function.Supplier;

public interface IdGenerationContext {
    <T extends DomainId<?>> Supplier<T> next(Class<T> type);
}
