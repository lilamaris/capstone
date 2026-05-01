package com.lilamaris.capstone.identity.auth.application.role.port.in.command;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;

import java.util.UUID;

public record RevokeRoleCommand(
        UUID userId,
        String namespaceName,
        CanonicalRole role,
        Actor requester
) {
}
