package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.RegisterCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;

public interface RegisterCredentialAccountUseCase {
    AuthenticationResult register(RegisterCredentialAccountCommand command);
}
