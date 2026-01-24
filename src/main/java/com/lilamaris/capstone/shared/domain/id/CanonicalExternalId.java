package com.lilamaris.capstone.shared.domain.id;

public record CanonicalExternalId(
        String value
) {
    public static CanonicalExternalId from(ExternalizableId externalId) {
        return new CanonicalExternalId(externalId.asString());
    }
}
