package com.lilamaris.capstone.domain.model.auth.access_control.id;

import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public class AccessControlIdSpec implements IdSpec<AccessControlId, UUID> {
    @Override
    public AccessControlId fromRaw(UUID raw) {
        return new AccessControlId(raw);
    }
}
