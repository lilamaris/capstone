package com.lilamaris.capstone.application.util.policy;

public interface DomainPolicyContext {
    boolean can(DomainRole role, DomainAction action);
}
