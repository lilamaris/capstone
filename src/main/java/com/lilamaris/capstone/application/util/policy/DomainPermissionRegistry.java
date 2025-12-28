package com.lilamaris.capstone.application.util.policy;

import java.util.Set;

public interface DomainPermissionRegistry {
    void register(DomainRole role, Set<DomainAction> action);

    boolean can(DomainRole role, DomainAction action);
}
