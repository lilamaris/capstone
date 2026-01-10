package com.lilamaris.capstone.shared.application.policy.access_control.port.in;

public interface ResourceAuthorityManageable {
    ResourceAction grantAction();

    ResourceAction revokeAction();
}
