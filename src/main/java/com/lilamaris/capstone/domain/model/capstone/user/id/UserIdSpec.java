package com.lilamaris.capstone.domain.model.capstone.user.id;

import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public final class UserIdSpec implements IdSpec<UserId, UUID> {
    @Override
    public UserId fromRaw(UUID raw) {
        return new UserId(raw);
    }
}
