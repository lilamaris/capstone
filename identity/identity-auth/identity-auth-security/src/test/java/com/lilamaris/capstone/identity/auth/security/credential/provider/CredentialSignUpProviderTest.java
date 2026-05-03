package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.RegisterCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.security.TestSupport;
import com.lilamaris.capstone.identity.auth.security.credential.filter.CredentialSignUpAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CredentialSignUpProvider 테스트")
class CredentialSignUpProviderTest {
    @Mock
    RegisterCredentialAccountUseCase useCase;

    private final CredentialSignUpAuthentication authentication =
            CredentialSignUpAuthentication.of(
                    TestSupport.NICKNAME,
                    TestSupport.EMAIL,
                    TestSupport.RAW_PASSWORD
            );

    @Nested
    @DisplayName("인증")
    class AuthenticateTest {
        @Test
        @DisplayName("credential 회원가입 결과를 중간 인증으로 변환한다")
        void convert_registration_result_to_credential_authentication() {
            var provider = provider();
            var authenticationResult = TestSupport.authenticationResult();
            when(useCase.register(authentication.toCommand())).thenReturn(authenticationResult);

            var authenticated = provider.authenticate(authentication);

            assertThat(authenticated).isInstanceOf(CredentialAuthenticate.class);
            assertThat(authenticated.isAuthenticated()).isTrue();
            assertThat(authenticated.getAuthorities()).isEmpty();
            assertThat(authenticated.getPrincipal()).isEqualTo(authenticationResult);
            assertThat(authenticated.getCredentials()).isNull();
        }

        @Test
        @DisplayName("이메일 중복 애플리케이션 예외는 AuthenticationServiceException으로 변환한다")
        void convert_email_duplicated_to_authentication_service_exception() {
            var provider = provider();
            when(useCase.register(authentication.toCommand()))
                    .thenThrow(new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.CREDENTIAL_EMAIL_DUPLICATED));

            assertThatThrownBy(() -> provider.authenticate(authentication))
                    .isInstanceOf(AuthenticationServiceException.class);
        }

        @Test
        @DisplayName("이메일 중복 외 애플리케이션 예외는 AuthenticationServiceException으로 변환한다")
        void convert_other_application_exception_to_authentication_service_exception() {
            var provider = provider();
            when(useCase.register(authentication.toCommand()))
                    .thenThrow(new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND));

            assertThatThrownBy(() -> provider.authenticate(authentication))
                    .isInstanceOf(AuthenticationServiceException.class)
                    .hasMessageContaining("Credential sign up failed");
        }
    }

    private CredentialSignUpProvider provider() {
        return new CredentialSignUpProvider(useCase);
    }
}
