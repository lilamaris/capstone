package com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in;

public interface ResourceAuthorityManageable {
    ResourceAction grantAction();

    ResourceAction revokeAction();
}
