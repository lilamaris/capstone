package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.User;

public record AuthIdentity(
        User user,
        Account account,
        boolean isCreated
) {
}
