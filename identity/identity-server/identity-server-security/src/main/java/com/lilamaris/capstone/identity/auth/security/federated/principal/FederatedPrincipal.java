package com.lilamaris.capstone.identity.auth.security.federated.principal;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateFederatedAccountCommand;

public interface FederatedPrincipal {
    String registrationId();

    String providerUserId();

    String providerUserNickname();

    default AuthenticateFederatedAccountCommand toCommand() {
        return new AuthenticateFederatedAccountCommand(providerUserNickname(), registrationId(), providerUserId());
    }
}
