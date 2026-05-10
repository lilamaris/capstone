package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;

public interface AuthenticateCredentialAccountUseCase {
    AuthenticationResult authenticate(AuthenticateCredentialAccountCommand command);
}
