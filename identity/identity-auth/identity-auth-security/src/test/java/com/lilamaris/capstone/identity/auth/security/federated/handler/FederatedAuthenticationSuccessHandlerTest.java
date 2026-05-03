package com.lilamaris.capstone.identity.auth.security.federated.handler;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.security.TestSupport;
import com.lilamaris.capstone.identity.auth.security.shared.principal.TrustedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FederatedAuthenticationSuccessHandler 테스트")
class FederatedAuthenticationSuccessHandlerTest {
    @Mock
    AuthenticateFederatedAccountUseCase useCase;

    @Mock
    TokenResponseProcessor tokenResponseProcessor;

    @Nested
    @DisplayName("인증 성공")
    class AuthenticationSuccessTest {
        @Test
        @DisplayName("federated principal로 애플리케이션 인증 후 TrustedPrincipal로 변환해서 토큰 응답을 처리한다")
        void authenticate_federated_principal_and_convert_to_trusted_principal() throws Exception {
            var response = new MockHttpServletResponse();
            var principal = TestSupport.federatedPrincipal();
            var handler = new FederatedAuthenticationSuccessHandler(
                    useCase,
                    new SeparatorBasedNamespaceRoleSerializer(),
                    tokenResponseProcessor
            );

            when(useCase.authenticate(new AuthenticateFederatedAccountCommand(
                    TestSupport.NICKNAME,
                    TestSupport.REGISTRATION_ID,
                    TestSupport.PROVIDER_USER_ID
            ))).thenReturn(TestSupport.authenticationResult());

            handler.onAuthenticationSuccess(
                    null,
                    response,
                    new TestingAuthenticationToken(principal, null)
            );

            var principalCaptor = ArgumentCaptor.forClass(TrustedPrincipal.class);
            verify(tokenResponseProcessor).process(
                    org.mockito.ArgumentMatchers.same(response),
                    principalCaptor.capture()
            );

            var trustedPrincipal = principalCaptor.getValue();
            assertThat(trustedPrincipal.userId()).isEqualTo(TestSupport.USER_ID);
            assertThat(trustedPrincipal.nickname()).isEqualTo(TestSupport.NICKNAME);
            assertThat(trustedPrincipal.scopes()).containsExactly(TestSupport.SCOPE);
        }
    }
}
