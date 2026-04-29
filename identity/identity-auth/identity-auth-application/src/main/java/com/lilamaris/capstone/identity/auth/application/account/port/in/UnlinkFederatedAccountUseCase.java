package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.UnlinkFederatedAccountCommand;

public interface UnlinkFederatedAccountUseCase {
    void unlink(UnlinkFederatedAccountCommand command);
}
