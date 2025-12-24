package com.lilamaris.capstone.domain.model.auth.refreshToken.id;

import com.lilamaris.capstone.domain.model.common.id.IdSpec;

public class RefreshTokenIdSpec implements IdSpec<RefreshTokenId, String> {
    @Override
    public RefreshTokenId fromRaw(String raw) {
        return new RefreshTokenId(raw);
    }
}
