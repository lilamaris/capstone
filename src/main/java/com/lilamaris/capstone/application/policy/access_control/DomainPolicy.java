package com.lilamaris.capstone.application.policy.access_control;

public interface DomainPolicy<R extends DomainRole> {
    boolean allows(DomainRole role, DomainAction action);

    R parseRole(String scopedRole);
}
