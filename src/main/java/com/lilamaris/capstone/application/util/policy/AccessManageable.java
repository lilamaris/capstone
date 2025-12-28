package com.lilamaris.capstone.application.util.policy;

public interface AccessManageable {
    DomainAction grantAction();

    DomainAction revokeAction();
}
