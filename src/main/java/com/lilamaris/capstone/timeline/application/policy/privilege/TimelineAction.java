package com.lilamaris.capstone.timeline.application.policy.privilege;

import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAction;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorityManageable;

public enum TimelineAction implements ResourceAction, ResourceAuthorityManageable {
    UPDATE_METADATA,
    MIGRATE,
    MERGE,
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
