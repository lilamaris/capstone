package com.lilamaris.capstone.shared.application.access_control.contract;

public interface AccessManageable {
    DomainAction grantAction();

    DomainAction revokeAction();
}
