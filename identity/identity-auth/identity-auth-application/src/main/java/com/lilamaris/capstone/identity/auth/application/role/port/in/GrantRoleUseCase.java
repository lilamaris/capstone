package com.lilamaris.capstone.identity.auth.application.role.port.in;

import com.lilamaris.capstone.identity.auth.application.role.port.in.command.GrantRoleCommand;
import com.lilamaris.capstone.identity.auth.application.role.port.in.result.UserGrantedRoleResult;

public interface GrantRoleUseCase {
    UserGrantedRoleResult grant(GrantRoleCommand command);
}
