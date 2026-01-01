package com.lilamaris.capstone.orchestration.auth.resolver;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.user.domain.User;

public record AuthIdentity(
        User user,
        Account account,
        boolean isCreated
) {
}
