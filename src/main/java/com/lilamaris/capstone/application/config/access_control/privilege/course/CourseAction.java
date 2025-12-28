package com.lilamaris.capstone.application.config.access_control.privilege.course;

import com.lilamaris.capstone.application.util.policy.AccessManageable;
import com.lilamaris.capstone.application.util.policy.DomainAction;

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
