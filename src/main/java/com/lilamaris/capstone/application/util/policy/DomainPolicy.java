package com.lilamaris.capstone.application.util.policy;

public interface DomainPolicy<R extends DomainRole> {
    boolean allows(DomainRole role, DomainAction action);

    R parseRole(String scopedRole);
}
