package com.lilamaris.capstone.shared.application.access_control.contract;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainPolicy<R extends DomainRole> {
    boolean allows(DomainRole role, DomainAction action);

    R parseRole(String scopedRole);

    DomainType supportType();
}
