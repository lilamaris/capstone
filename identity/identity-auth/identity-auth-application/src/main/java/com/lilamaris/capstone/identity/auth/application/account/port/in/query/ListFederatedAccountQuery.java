package com.lilamaris.capstone.identity.auth.application.account.port.in.query;

import java.util.UUID;

public record ListFederatedAccountQuery(
        UUID userId
) {
}
