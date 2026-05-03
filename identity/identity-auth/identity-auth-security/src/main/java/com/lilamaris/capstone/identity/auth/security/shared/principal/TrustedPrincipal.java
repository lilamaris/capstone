package com.lilamaris.capstone.identity.auth.security.shared.principal;

import java.util.Set;
import java.util.UUID;

public interface TrustedPrincipal {
    UUID userId();

    String nickname();

    Set<String> scopes();
}
