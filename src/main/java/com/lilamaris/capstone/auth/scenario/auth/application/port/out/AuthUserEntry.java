package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

import com.lilamaris.capstone.auth.user.domain.User;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record AuthUserEntry(
        ExternalizableId id,
        String displayName
) {
    public static AuthUserEntry from(User user) {
        return new AuthUserEntry(
                user.id().externalId(),
                user.getDisplayName()
        );
    }
}
