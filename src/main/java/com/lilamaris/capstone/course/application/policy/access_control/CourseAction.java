package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.access_control.contract.AccessManageable;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainAction;

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
