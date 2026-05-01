package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;

public interface AuthenticateFederatedAccountUseCase {
    AuthenticationResult authenticate(AuthenticateFederatedAccountCommand command);
}
