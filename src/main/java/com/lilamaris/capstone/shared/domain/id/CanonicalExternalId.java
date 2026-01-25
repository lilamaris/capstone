package com.lilamaris.capstone.shared.domain.id;

import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;

public record CanonicalExternalId(
        String value
) {
    public static CanonicalExternalId from(ExternalizableId externalId) {
        return new CanonicalExternalId(externalId.asString());
    }

    public ExternalizableId toExternalizableId() {
        return new DefaultExternalizableId(value);
    }
}
