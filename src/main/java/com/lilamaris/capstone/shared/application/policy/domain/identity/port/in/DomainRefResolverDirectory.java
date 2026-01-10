package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

public interface DomainRefResolverDirectory {
    <T extends DomainId<?>> T resolve(DomainRef ref);
}
