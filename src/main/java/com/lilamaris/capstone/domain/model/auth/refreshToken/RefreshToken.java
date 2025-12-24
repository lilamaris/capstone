package com.lilamaris.capstone.domain.model.auth.refreshToken;

import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import lombok.Getter;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
public class RefreshToken implements Identifiable<RefreshTokenId> {
    private final RefreshTokenId token;
    private final UserId userId;

    protected RefreshToken(RefreshTokenId id, UserId userId) {
        this.token = requireField(id, "id");
        this.userId = requireField(userId, "userId");
    }

    @Override
    public RefreshTokenId id() {
        return token;
    }
}
