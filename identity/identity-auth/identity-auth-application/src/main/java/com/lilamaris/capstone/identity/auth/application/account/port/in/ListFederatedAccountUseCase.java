package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.ListFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.FederatedAccountResult;

import java.util.List;

public interface ListFederatedAccountUseCase {
    List<FederatedAccountResult> list(ListFederatedAccountCommand command);
}
