package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

public class AccountResult {
    @Builder
    public record Command(
            Account.Id id,
            User.Id userId,
            Provider provider,
            String providerId,
            String displayName,
            String email
    ) {
        public static Command from(Account domain) {
            return builder()
                    .id(domain.id())
                    .userId(domain.userId())
                    .provider(domain.provider())
                    .providerId(domain.providerId())
                    .displayName(domain.displayName())
                    .email(domain.email())
                    .build();
        }
    }

    @Builder
    public record Query(
            Account.Id id,
            User.Id userId,
            Provider provider,
            String providerId,
            String displayName,
            String email
    ) {
        public static Query from(Account domain) {
            return builder()
                    .id(domain.id())
                    .userId(domain.userId())
                    .provider(domain.provider())
                    .providerId(domain.providerId())
                    .displayName(domain.displayName())
                    .email(domain.email())
                    .build();
        }
    }
}
