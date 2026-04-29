package com.lilamaris.capstone.identity.auth.application.account.port.in.command;

public record RegisterCredentialAccountCommand(
        String nickname,
        String email,
        String password
) {
}
