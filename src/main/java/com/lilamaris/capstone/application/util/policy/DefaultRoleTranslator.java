package com.lilamaris.capstone.application.util.policy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRoleTranslator<R extends Enum<R> & DomainRole> implements RoleTranslator {
    private final Class<R> roleType;

    @Override
    public DomainRole toRoleName(String scopedRole) {
        return Enum.valueOf(roleType, scopedRole);
    }
}
