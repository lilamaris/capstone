package com.lilamaris.capstone.access_control.application.port.in;

public interface AccessManageable {
    DomainAction grantAction();

    DomainAction revokeAction();
}
