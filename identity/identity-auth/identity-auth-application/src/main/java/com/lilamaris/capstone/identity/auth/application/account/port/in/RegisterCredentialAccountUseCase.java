package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.RegisterCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;

public interface RegisterCredentialAccountUseCase {
    UserResult register(RegisterCredentialAccountCommand command);
}
