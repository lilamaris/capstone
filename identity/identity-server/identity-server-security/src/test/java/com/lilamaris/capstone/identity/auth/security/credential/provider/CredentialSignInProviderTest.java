package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.security.TestSupport;
import com.lilamaris.capstone.identity.auth.security.credential.filter.CredentialSignInAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CredentialSignInProvider 테스트")
class CredentialSignInProviderTest {
    @Mock
    AuthenticateCredentialAccountUseCase useCase;

    private final CredentialSignInAuthentication authentication =
            CredentialSignInAuthentication.of(TestSupport.EMAIL, TestSupport.RAW_PASSWORD);

    private CredentialSignInProvider provider() {
        return new CredentialSignInProvider(useCase);
    }

    @Nested
    @DisplayName("인증")
    class AuthenticateTest {
        @Test
        @DisplayName("credential 계정 인증 결과를 중간 인증으로 변환한다")
        void convert_authentication_result_to_credential_authentication() {
            var provider = provider();
            var authenticationResult = TestSupport.authenticationResult();
            when(useCase.authenticate(authentication.toCommand())).thenReturn(authenticationResult);

            var authenticated = provider.authenticate(authentication);

            assertThat(authenticated).isInstanceOf(CredentialAuthenticate.class);
            assertThat(authenticated.isAuthenticated()).isTrue();
            assertThat(authenticated.getAuthorities()).isEmpty();
            assertThat(authenticated.getPrincipal()).isEqualTo(authenticationResult);
            assertThat(authenticated.getCredentials()).isNull();
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

}
