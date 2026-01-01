package com.lilamaris.capstone.shared.application.identity;

import com.lilamaris.capstone.shared.domain.id.DomainId;

import java.util.function.Supplier;

public interface IdGenerationContext {
    <T extends DomainId<?>> Supplier<T> next(Class<T> type);
}
