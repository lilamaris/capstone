package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAction;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorityManageable;

public enum CourseAction implements ResourceAction, ResourceAuthorityManageable {
    READ,
    UPDATE_METADATA,
    OFFER,
    GRANT_ROLE,
    REVOKE_ROLE;

    @Override
    public ResourceAction grantAction() {
        return GRANT_ROLE;
    }

    @Override
    public ResourceAction revokeAction() {
        return REVOKE_ROLE;
    }
}
