package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.ChangeCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;

public interface ChangeCredentialPasswordUseCase {
    UserResult change(ChangeCredentialAccountCommand command);
}
