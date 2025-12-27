package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;

public record DefaultExternalizableId(String id) implements ExternalizableId {
    @Override
    public String asString() {
        return id;
    }
}
