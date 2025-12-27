package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

public record DefaultDomainRef(DomainType type, ExternalizableId id) implements DomainRef {
}
