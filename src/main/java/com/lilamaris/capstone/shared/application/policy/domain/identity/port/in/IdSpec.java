package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;

@FunctionalInterface
public interface IdSpec<T extends DomainId<?>, R> {
    T fromRaw(R raw);
}
