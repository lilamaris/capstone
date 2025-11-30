package com.lilamaris.capstone.application.port.in.command;

import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

public class AuthCommand {
    @Builder
    public record CreateOrLoginOidc(
            Provider provider,
            String providerId,
            String displayName
    ) {}

    @Builder
    public record LinkOidc(
            User.Id userId,
            Provider provider,
            String providerId,
            String displayName
    ) {}

    @Builder
    public record CreateCredential(
            User.Id userId,
            String email,
            String passwordHash
    ) {}
}
