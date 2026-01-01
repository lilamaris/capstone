package com.lilamaris.capstone.refresh_token.domain;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.Getter;

import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
public class RefreshToken implements Identifiable<RefreshTokenId> {
    private final RefreshTokenId token;
    private final UserId userId;

    protected RefreshToken(RefreshTokenId id, UserId userId) {
        this.token = requireField(id, "id");
        this.userId = requireField(userId, "userId");
    }

    public static RefreshToken create(UserId userId, Supplier<RefreshTokenId> idSupplier) {
        return new RefreshToken(idSupplier.get(), userId);
    }

    @Override
    public RefreshTokenId id() {
        return token;
    }
}
