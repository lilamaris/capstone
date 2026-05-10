package com.lilamaris.capstone.identity.auth.security;

import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.security.federated.principal.FederatedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.principal.SimpleTrustedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.principal.TrustedPrincipal;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.SimpleNamespaceRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;

import java.util.Set;
import java.util.UUID;

public final class TestSupport {
    public static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public static final String NICKNAME = "tester";
    public static final String EMAIL = "tester@example.com";
    public static final String RAW_PASSWORD = "raw-password";

    public static final String REGISTRATION_ID = "google";
    public static final String PROVIDER_USER_ID = "google-user-1";

    public static final String NAMESPACE_NAME = "identity-auth";
    public static final CanonicalRole ROLE = CanonicalRole.USER;
    public static final String SCOPE = "identity-auth:USER";

    private TestSupport() {
    }

    public static AuthenticationResult authenticationResult() {
        return new AuthenticationResult(
                USER_ID,
                NICKNAME,
                Set.of(SimpleNamespaceRole.of(
                        SimpleApplicationNamespace.of(NAMESPACE_NAME),
                        ROLE
                ))
        );
    }

    public static FederatedPrincipal federatedPrincipal() {
        return new FederatedPrincipal() {
            @Override
            public String registrationId() {
                return REGISTRATION_ID;
            }

            @Override
            public String providerUserId() {
                return PROVIDER_USER_ID;
            }

            @Override
            public String providerUserNickname() {
                return NICKNAME;
            }
        };
    }

    public static TrustedPrincipal trustedPrincipal() {
        return SimpleTrustedPrincipal.of(USER_ID, NICKNAME, Set.of(SCOPE));
    }

    public static String credentialSignInRequestBody() {
        return """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(EMAIL, RAW_PASSWORD);
    }

    public static String credentialSignUpRequestBody() {
        return """
                {
                  "nickname": "%s",
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(NICKNAME, EMAIL, RAW_PASSWORD);
    }
}
