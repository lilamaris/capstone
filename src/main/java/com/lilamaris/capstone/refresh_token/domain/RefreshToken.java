package com.lilamaris.capstone.refresh_token.domain;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

public record RefreshToken(String principal) {
    public RefreshToken(
            String principal
    ) {
        this.principal = requireField(principal, "principal");
    }

    public static RefreshToken create(ExternalizableId principal) {
        return new RefreshToken(principal.asString());
    }
}
