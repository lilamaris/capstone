package com.lilamaris.capstone.application.policy.access_control;

public interface AccessManageable {
    DomainAction grantAction();

    DomainAction revokeAction();
}
