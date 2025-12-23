package com.lilamaris.capstone.domain.model.auth;

import com.lilamaris.capstone.domain.model.auth.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

public class RefreshToken implements Identifiable<RefreshTokenId> {
    private final RefreshTokenId token;
    private final String userId;

    private RefreshToken(RefreshTokenId id, String userId) {
        this.token = requireField(id, "id");
        this.userId = requireField(userId, "userId");
    }

    @Override
    public RefreshTokenId id() {
        return token;
    }
}
