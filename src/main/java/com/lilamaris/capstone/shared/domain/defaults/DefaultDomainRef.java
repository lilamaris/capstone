package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public record DefaultDomainRef(DomainType type, ExternalizableId id) implements DomainRef {
}
