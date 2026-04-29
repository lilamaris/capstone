package com.lilamaris.capstone.identity.auth.application.account.port.in.command;

import java.util.UUID;

public record ChangeCredentialAccountCommand(
        UUID userId,
        String oldPassword,
        String newPassword
) {
}
