package com.lilamaris.capstone.identity.auth.application.role.port.in;

import com.lilamaris.capstone.identity.auth.application.role.port.in.command.RevokeRoleCommand;

public interface RevokeRoleUseCase {
    void revoke(RevokeRoleCommand command);
}
