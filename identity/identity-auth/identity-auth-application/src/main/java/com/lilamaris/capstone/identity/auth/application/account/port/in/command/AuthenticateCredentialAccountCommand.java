package com.lilamaris.capstone.identity.auth.application.account.port.in.command;

public record AuthenticateCredentialAccountCommand(
        String email,
        String password
) {
}
