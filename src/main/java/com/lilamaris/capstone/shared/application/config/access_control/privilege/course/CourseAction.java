package com.lilamaris.capstone.shared.application.config.access_control.privilege.course;

import com.lilamaris.capstone.access_control.application.port.in.AccessManageable;
import com.lilamaris.capstone.access_control.application.port.in.DomainAction;

public enum CourseAction implements DomainAction, AccessManageable {
    READ,
    UPDATE_METADATA,
    OFFER,
    GRANT_ROLE,
    REVOKE_ROLE;

    @Override
    public DomainAction grantAction() {
        return GRANT_ROLE;
    }

    @Override
    public DomainAction revokeAction() {
        return REVOKE_ROLE;
    }
}
