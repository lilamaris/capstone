package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.user.domain.User;

public record UserEntry(
        ExternalizableId id,
        String displayName
) {
    public static UserEntry from(User user) {
        return new UserEntry(
                user.id(),
                user.getDisplayName()
        );
    }
}
