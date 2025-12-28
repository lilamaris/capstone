package com.lilamaris.capstone.domain.model.common.domain.policy;

public interface DomainPolicyContext {
    boolean can(DomainRole role, DomainAction action);
}
