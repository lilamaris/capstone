package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.User;

public record AuthIdentity(
        User user,
        Account account,
        boolean isCreated
) {
}
