package com.lilamaris.capstone.access_control.application.port.in;

public interface DomainPolicy<R extends DomainRole> {
    boolean allows(DomainRole role, DomainAction action);

    R parseRole(String scopedRole);
}
