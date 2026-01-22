package com.lilamaris.capstone.shared.application.result;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

public record DomainRefResult(
        String type,
        String id
) {
    public static DomainRefResult from(DomainRef ref) {
        return new DomainRefResult(
                ref.type().name(),
                ref.id().asString()
        );
    }
}
