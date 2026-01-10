package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainRefResolver<T extends DomainId<?>> {
    DomainType supports();

    T resolve(DomainRef ref);
}
