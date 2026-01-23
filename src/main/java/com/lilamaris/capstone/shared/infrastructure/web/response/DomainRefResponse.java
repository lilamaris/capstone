package com.lilamaris.capstone.shared.infrastructure.web.response;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

public record DomainRefResponse(
        String id,
        String type
) {
    public static DomainRefResponse from(DomainRef ref) {
        return new DomainRefResponse(
                ref.id().asString(),
                ref.type().name()
        );
    }
}
