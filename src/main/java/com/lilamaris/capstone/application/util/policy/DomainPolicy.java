package com.lilamaris.capstone.application.util.policy;

import java.util.Set;

public interface DomainPolicy {
    void allow(DomainRole role, Set<DomainAction> action);

    boolean allows(DomainRole role, DomainAction action);
}
