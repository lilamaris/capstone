package com.lilamaris.capstone.account.application.result;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.user.domain.id.UserId;

public class AccountResult {
    public record Command(
            AccountId id,
            UserId userId,
            Provider provider,
            String providerId,
            String displayName,
            String email
    ) {
        public static Command from(Account domain) {
            return new Command(
                    domain.id(),
                    domain.getUserId(),
                    domain.getProvider(),
                    domain.getProviderId(),
                    domain.getDisplayName(),
                    domain.getEmail()
            );
        }
    }

    public record Query(
            AccountId id,
            UserId userId,
            Provider provider,
            String providerId,
            String displayName,
            String email
    ) {
        public static Query from(Account domain) {
            return new Query(
                    domain.id(),
                    domain.getUserId(),
                    domain.getProvider(),
                    domain.getProviderId(),
                    domain.getDisplayName(),
                    domain.getEmail()
            );
        }
    }
}
