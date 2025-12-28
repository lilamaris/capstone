package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.capstone.user.User;

public record AuthIdentity(
        User user,
        Account account,
        boolean isCreated
) {
}
