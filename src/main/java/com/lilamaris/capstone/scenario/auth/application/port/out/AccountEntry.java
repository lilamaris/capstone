package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record AccountEntry(
        ExternalizableId id,
        ExternalizableId userId,
        String displayName
) {
    public static AccountEntry from(Account account) {
        return new AccountEntry(
                account.id(),
                account.getUserId(),
                account.getDisplayName()
        );
    }
}
