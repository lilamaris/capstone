package com.lilamaris.capstone.identity.auth.application.account.port.in.result;

import com.lilamaris.capstone.identity.auth.domain.account.User;

import java.time.Instant;
import java.util.UUID;

public record UserResult(
        UUID id,
        String nickname,
        Instant createdAt
) {
    public static UserResult from(User user) {
        return new UserResult(user.getId(), user.getNickname(), user.getCreatedAt());
    }
}
