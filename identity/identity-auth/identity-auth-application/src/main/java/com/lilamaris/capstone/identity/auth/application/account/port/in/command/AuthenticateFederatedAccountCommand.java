package com.lilamaris.capstone.identity.auth.application.account.port.in.command;

public record AuthenticateFederatedAccountCommand(
        String providerUserNickname,
        String registrationId,
        String providerUserId
) {
}
