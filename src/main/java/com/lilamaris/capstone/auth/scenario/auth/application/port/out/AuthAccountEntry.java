package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

import com.lilamaris.capstone.auth.account.domain.Account;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record AuthAccountEntry(
        ExternalizableId id,
        ExternalizableId userId
) {
    public static AuthAccountEntry from(Account account) {
        return new AuthAccountEntry(
                account.id().externalId(),
                account.getUserId().externalId()
        );
    }
}
