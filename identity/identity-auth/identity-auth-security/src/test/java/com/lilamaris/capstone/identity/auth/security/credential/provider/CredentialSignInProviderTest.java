package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.security.credential.filter.CredentialSignInAuthentication;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import com.lilamaris.capstone.identity.core.role.SimpleNamespaceRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CredentialSignInProvider 테스트")
class CredentialSignInProviderTest {
    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    AuthenticateCredentialAccountUseCase useCase;

    private final CredentialSignInAuthentication authentication =
            CredentialSignInAuthentication.of("tester@example.com", "raw-password");

    @Nested
    @DisplayName("인증")
    class AuthenticateTest {
        @Test
        @DisplayName("credential 계정 인증 결과를 신뢰된 인증으로 변환한다")
        void convert_authentication_result_to_trusted_authentication() {
            var provider = provider();
            when(useCase.authenticate(authentication.toCommand())).thenReturn(authenticationResult());

            var authenticated = provider.authenticate(authentication);

            assertThat(authenticated).isInstanceOf(CredentialTrustedAuthenticate.class);
            assertThat(authenticated.isAuthenticated()).isTrue();
            assertThat(authenticated.getAuthorities())
                    .extracting(Object::toString)
                    .containsExactly("identity-auth:USER");

            var trusted = (CredentialTrustedAuthenticate) authenticated;
            assertThat(trusted.userId()).isEqualTo(USER_ID);
            assertThat(trusted.nickname()).isEqualTo("tester");
            assertThat(trusted.scopes()).containsExactly("identity-auth:USER");
        }

        @Test
        @DisplayName("인증 실패 애플리케이션 예외는 BadCredentialsException으로 변환한다")
        void convert_authentication_failed_to_bad_credentials_exception() {
            var provider = provider();
            when(useCase.authenticate(authentication.toCommand()))
                    .thenThrow(new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED));

            assertThatThrownBy(() -> provider.authenticate(authentication))
                    .isInstanceOf(BadCredentialsException.class);
        }

        @Test
        @DisplayName("인증 실패 외 애플리케이션 예외는 AuthenticationServiceException으로 변환한다")
        void convert_other_application_exception_to_authentication_service_exception() {
            var provider = provider();
            when(useCase.authenticate(authentication.toCommand()))
                    .thenThrow(new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND));

            assertThatThrownBy(() -> provider.authenticate(authentication))
                    .isInstanceOf(AuthenticationServiceException.class)
                    .hasMessageContaining("Credential sign in failed.");
        }
    }

    private CredentialSignInProvider provider() {
        return new CredentialSignInProvider(useCase, new SeparatorBasedNamespaceRoleSerializer());
    }

    private AuthenticationResult authenticationResult() {
        return new AuthenticationResult(
                USER_ID,
                "tester",
                Set.of(SimpleNamespaceRole.of(
                        SimpleApplicationNamespace.of("identity-auth"),
                        CanonicalRole.USER
                ))
        );
    }
}
