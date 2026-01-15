package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.user.domain.User;

public record AuthUserEntry(
        ExternalizableId id,
        String displayName
) {
    public static AuthUserEntry from(User user) {
        return new AuthUserEntry(
                user.id(),
                user.getDisplayName()
        );
    }
}
