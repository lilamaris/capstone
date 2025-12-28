package com.lilamaris.capstone.application.util.policy;

public interface RoleTranslator {
    DomainRole toRoleName(String scopedRole);
}
