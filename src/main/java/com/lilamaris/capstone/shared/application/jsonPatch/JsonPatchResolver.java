package com.lilamaris.capstone.shared.application.jsonPatch;

import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface JsonPatchResolver<T extends Identifiable<?>> {
    DomainType support();

    String resolve(T domain);

    String resolve(DomainRef ref);
}
