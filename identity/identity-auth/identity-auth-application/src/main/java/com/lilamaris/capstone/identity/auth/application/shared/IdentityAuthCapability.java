package com.lilamaris.capstone.identity.auth.application.shared;

import com.lilamaris.capstone.identity.core.actor.Capability;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IdentityAuthCapability implements Capability {
    GRANT_ROLE("role.grant", "역할 부여"),
    REVOKE_ROLE("role.revoke", "역할 회수");

    private final String scope;
    private final String description;

    @Override
    public String scope() {
        return scope;
    }

    @Override
    public String description() {
        return description;
    }
}
