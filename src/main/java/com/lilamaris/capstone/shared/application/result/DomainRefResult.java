package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public record DomainRefResult(
        DomainType type,
        ExternalizableId id
) {
    public static DomainRefResult from(DomainRef ref) {
        return new DomainRefResult(
                ref.type(),
                ref.id()
        );
    }
}
