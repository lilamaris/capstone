package com.lilamaris.capstone.shared.application.policy.domain.visibility.port.in;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainVisibilityPolicy {
    DomainType support();

    DomainRole discoverableRole();

    DomainRole readableRole();
}
