package com.lilamaris.capstone.identity.auth.application.account.port.in.command;

import java.util.UUID;

public record LinkFederatedAccountCommand(
        UUID userId,
        String registrationId,
        String providerUserId
) {
}
