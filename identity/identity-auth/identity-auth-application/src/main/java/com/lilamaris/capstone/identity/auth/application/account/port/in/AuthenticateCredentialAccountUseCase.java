package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;

public interface AuthenticateCredentialAccountUseCase {
    UserResult authenticate(AuthenticateCredentialAccountCommand command);
}
