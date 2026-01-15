package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record DefaultExternalizableId(
        String id
) implements ExternalizableId {
    public static DefaultExternalizableId from(ExternalizableId id) {
        return new DefaultExternalizableId(id.asString());
    }

    public static DefaultExternalizableId from(String id) {
        return new DefaultExternalizableId(id);
    }

    @Override
    public String asString() {
        return id;
    }
}
