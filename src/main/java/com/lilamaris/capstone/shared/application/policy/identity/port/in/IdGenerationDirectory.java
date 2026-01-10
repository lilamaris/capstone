package com.lilamaris.capstone.shared.application.policy.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;

import java.util.function.Supplier;

public interface IdGenerationDirectory {
    <T extends DomainId<?>> Supplier<T> next(Class<T> type);
}
