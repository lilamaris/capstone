package com.lilamaris.capstone.identity.auth.security.shared.response;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.security.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenResponseProcessor 테스트")
class TokenResponseProcessorTest {
    @Mock
    IssueJwtUseCase issueJwtUseCase;

    @Mock
    IssueOpaqueTokenUseCase issueOpaqueTokenUseCase;

    @Nested
    @DisplayName("응답 처리")
    class ProcessTest {
        @Test
        @DisplayName("principal의 subject와 scope로 토큰을 발급하고 응답한다")
        void issue_token_and_write_response() throws Exception {
            var response = new MockHttpServletResponse();
            var processor = new TokenResponseProcessor(
                    issueJwtUseCase,
                    issueOpaqueTokenUseCase,
                    new ResponseWriter(new ObjectMapper())
            );
            var principal = TestSupport.trustedPrincipal();

            when(issueJwtUseCase.issue(TestSupport.USER_ID.toString(), principal.scopes()))
                    .thenReturn("access-token");
            when(issueOpaqueTokenUseCase.issue()).thenReturn("refresh-token");

            processor.process(response, principal);

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentType()).contains("application/json");
            assertThat(response.getContentAsString()).contains("access-token");
            assertThat(response.getContentAsString()).contains("refresh-token");
        }
    }
}
