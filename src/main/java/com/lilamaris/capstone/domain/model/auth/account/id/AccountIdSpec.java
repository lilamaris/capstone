package com.lilamaris.capstone.domain.model.auth.account.id;

import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public class AccountIdSpec implements IdSpec<AccountId, UUID> {
    @Override
    public AccountId fromRaw(UUID raw) {
        return new AccountId(raw);
    }
}
