package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record AuthAccountEntry(
        ExternalizableId id,
        ExternalizableId userId
) {
    public static AuthAccountEntry from(Account account) {
        return new AuthAccountEntry(
                account.id(),
                account.getUserId()
        );
    }
}
