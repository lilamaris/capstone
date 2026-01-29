package com.lilamaris.capstone.access.resource.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import jakarta.annotation.Nullable;

public record ResourceReadOption(
        DomainType type,
        @Nullable ExternalizableId id
) {
    public ResourceReadOption {
        if (type == null) throw new IllegalArgumentException("Domain type can not be null.");
    }

    public static ResourceReadOption typeOnly(DomainType type) {
        return new ResourceReadOption(type, null);
    }

    public static ResourceReadOption full(DomainType type, ExternalizableId id) {
        return new ResourceReadOption(type, id);
    }

    public boolean hasId() {
        return id != null;
    }
}
