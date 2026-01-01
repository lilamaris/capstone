package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record DefaultExternalizableId(String id) implements ExternalizableId {
    @Override
    public String asString() {
        return id;
    }
}
