package com.lilamaris.capstone.identity.auth.security.credential.filter;

import com.lilamaris.capstone.identity.auth.security.exception.AuthenticationProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JsonCredentialSignUpProcessingFilter 테스트")
class JsonCredentialSignUpProcessingFilterTest {
    private final JsonCredentialSignUpProcessingFilter filter = new JsonCredentialSignUpProcessingFilter(
            PathPatternRequestMatcher.withDefaults().matcher("/auth/sign-up"),
            new ObjectMapper()
    );

    @Nested
    @DisplayName("인증 시도")
    class AttemptAuthenticationTest {
        @Test
        @DisplayName("요청 본문을 credential 회원가입 인증으로 변환한다")
        void convert_request_body_to_credential_sign_up_authentication() throws Exception {
            var captured = new AtomicReference<Authentication>();
            filter.setAuthenticationManager(authentication -> {
                captured.set(authentication);
                return authentication;
            });

            var request = request("""
                    {
                      "nickname": "tester",
                      "email": "tester@example.com",
                      "password": "raw-password"
                    }
                    """);

            var result = filter.attemptAuthentication(request, new MockHttpServletResponse());

            assertThat(result).isInstanceOf(CredentialSignUpAuthentication.class);
            assertThat(captured.get()).isInstanceOf(CredentialSignUpAuthentication.class);

            var authentication = (CredentialSignUpAuthentication) captured.get();
            assertThat(authentication.getPrincipal()).isEqualTo("tester@example.com");
            assertThat(authentication.getCredentials()).isEqualTo("raw-password");
            assertThat(authentication.toCommand().nickname()).isEqualTo("tester");
            assertThat(authentication.isAuthenticated()).isFalse();
        }

        @Test
        @DisplayName("요청 본문이 JSON 형식이 아니면 예외")
        void throw_exception_when_request_body_is_invalid_json() {
            filter.setAuthenticationManager(authentication -> authentication);

            var request = request("invalid-json");

            assertThatThrownBy(() -> filter.attemptAuthentication(request, new MockHttpServletResponse()))
                    .isInstanceOf(AuthenticationProcessingException.class)
                    .hasMessageContaining("Invalid credential sign-up request.");
        }

        @Test
        @DisplayName("password가 빈 문자열이면 예외")
        void throw_exception_when_password_is_blank() {
            filter.setAuthenticationManager(authentication -> authentication);

            var request = request("""
                    {
                      "nickname": "tester",
                      "email": "tester@example.com",
                      "password": " "
                    }
                    """);

            assertThatThrownBy(() -> filter.attemptAuthentication(request, new MockHttpServletResponse()))
                    .isInstanceOf(AuthenticationProcessingException.class)
                    .hasMessageContaining("Invalid credential sign-up request.");
        }
    }

    private MockHttpServletRequest request(String content) {
        var request = new MockHttpServletRequest("POST", "/auth/sign-up");
        request.setContentType("application/json");
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        request.setContent(content.getBytes(StandardCharsets.UTF_8));
        return request;
    }
}
