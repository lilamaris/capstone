package com.lilamaris.capstone.access.resource.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface ResourceRegister {
    DomainRef register(DomainType type, ExternalizableId id);
}