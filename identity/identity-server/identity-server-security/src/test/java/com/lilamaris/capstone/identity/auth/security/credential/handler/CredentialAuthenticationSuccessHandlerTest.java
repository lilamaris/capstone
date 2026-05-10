package com.lilamaris.capstone.identity.auth.security.credential.handler;

import com.lilamaris.capstone.identity.auth.security.TestSupport;
import com.lilamaris.capstone.identity.auth.security.credential.provider.CredentialAuthenticate;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CredentialAuthenticationSuccessHandler 테스트")
class CredentialAuthenticationSuccessHandlerTest {
    @Mock
    TokenResponseProcessor tokenResponseProcessor;

    @Nested
    @DisplayName("인증 성공")
    class AuthenticationSuccessTest {
        @Test
        @DisplayName("credential 인증 결과를 TrustedPrincipal로 변환해서 토큰 응답을 처리한다")
        void convert_credential_authentication_result_to_trusted_principal() throws Exception {
            var response = new MockHttpServletResponse();
            var handler = new CredentialAuthenticationSuccessHandler(
                    tokenResponseProcessor,
                    new SeparatorBasedNamespaceRoleSerializer()
            );

            handler.onAuthenticationSuccess(
                    null,
                    response,
                    CredentialAuthenticate.of(TestSupport.authenticationResult())
            );

            var principalCaptor = ArgumentCaptor.forClass(TrustedPrincipal.class);
            verify(tokenResponseProcessor).process(
                    org.mockito.ArgumentMatchers.same(response),
                    principalCaptor.capture()
            );

            var principal = principalCaptor.getValue();
            assertThat(principal.userId()).isEqualTo(TestSupport.USER_ID);
            assertThat(principal.nickname()).isEqualTo(TestSupport.NICKNAME);
            assertThat(principal.scopes()).containsExactly(TestSupport.SCOPE);
        }
    }
}
