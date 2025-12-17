package com.lilamaris.capstone.domain.auth;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

@Builder(toBuilder = true)
public record RefreshToken(
        Id id,
        User.Id userId
) implements BaseDomain<RefreshToken.Id, RefreshToken> {
    public record Id(String value) implements BaseDomain.Id<String> {
        public static Id from(String value) { return new Id(value); }
    }
}
