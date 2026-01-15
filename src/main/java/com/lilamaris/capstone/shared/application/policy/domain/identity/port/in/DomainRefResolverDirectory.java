package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainRefResolverDirectory {
    <T extends DomainId<?>> T resolve(DomainRef ref, Class<T> expect);

    <T extends DomainId<?>> T resolve(ExternalizableId externalId, DomainType type, Class<T> expect);
}
