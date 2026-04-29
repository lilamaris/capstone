package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.LinkFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;

public interface LinkFederatedAccountUseCase {
    UserResult link(LinkFederatedAccountCommand command);
}
