package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

public class AuthResult {
    @Builder
    public record Login(
        boolean created,
        UserResult.Query user,
        AccountResult.Query account
    ) {
        public static Login from(boolean created, User user, Account account) {
            return builder()
                    .created(created)
                    .user(UserResult.Query.from(user))
                    .account(AccountResult.Query.from(account))
                    .build();
        }
    }

    @Builder
    public record Register(
            UserResult.Query user,
            AccountResult.Query account
    ) {
        public static Register from(User user, Account account) {
            return builder()
                    .user(UserResult.Query.from(user))
                    .account(AccountResult.Query.from(account))
                    .build();
        }
    }

    @Builder
    public record Link(
            UserResult.Query user,
            AccountResult.Query account
    ) {
        public static Link from(User user, Account account) {
            return builder()
                    .user(UserResult.Query.from(user))
                    .account(AccountResult.Query.from(account))
                    .build();
        }
    }
}
