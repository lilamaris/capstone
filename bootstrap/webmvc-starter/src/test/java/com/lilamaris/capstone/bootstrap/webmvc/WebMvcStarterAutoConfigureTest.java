package com.lilamaris.capstone.bootstrap.webmvc;

import com.lilamaris.capstone.bootstrap.webmvc.advice.GlobalRestControllerExceptionAdvice;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.AccessDeniedExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.ApplicationExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.BadRequestExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.FallbackExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.IllegalStateExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.HttpStatusResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.TypeUriResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WebMvcStarterAutoConfigure 테스트")
class WebMvcStarterAutoConfigureTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WebMvcStarterAutoConfigure.class));

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("webmvc advice 기본 bean을 등록한다")
        void register_default_webmvc_advice_beans() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(TypeUriResolver.class);
                assertThat(context).hasSingleBean(ProblemDetailFactory.class);
                assertThat(context).hasSingleBean(HttpStatusResolver.class);
                assertThat(context).hasSingleBean(ApplicationExceptionHandler.class);
                assertThat(context).hasSingleBean(BadRequestExceptionHandler.class);
                assertThat(context).hasSingleBean(AccessDeniedExceptionHandler.class);
                assertThat(context).hasSingleBean(IllegalStateExceptionHandler.class);
                assertThat(context).hasSingleBean(FallbackExceptionHandler.class);
                assertThat(context).hasSingleBean(GlobalRestControllerExceptionAdvice.class);
            });
        }

        @Test
        @DisplayName("ApplicationBaseException을 ProblemDetail로 변환한다")
        void handles_application_base_exception() {
            contextRunner.run(context -> {
                var advice = context.getBean(GlobalRestControllerExceptionAdvice.class);
                var request = new MockHttpServletRequest("GET", "/timelines/unknown");
                var problem = advice.handleApplicationException(
                        new ApplicationBaseException(TestErrorCode.TIMELINE_NOT_FOUND),
                        request
                );

                assertThat(problem.getStatus()).isEqualTo(404);
                assertThat(problem.getTitle()).isEqualTo("TIMELINE_NOT_FOUND");
                assertThat(problem.getDetail()).isEqualTo("타임라인을 찾을 수 없습니다.");
                assertThat(problem.getType().toString()).isEqualTo("https://capstone.com/errors/TIMELINE_NOT_FOUND");
                assertThat(problem.getProperties()).containsEntry("code", "A001");
            });
        }

        @Test
        @DisplayName("AccessDeniedException을 403 ProblemDetail로 변환한다")
        void handles_access_denied_exception() {
            contextRunner.run(context -> {
                var advice = context.getBean(GlobalRestControllerExceptionAdvice.class);
                var request = new MockHttpServletRequest("GET", "/admin");
                var problem = advice.handleAccessDenied(new AccessDeniedException("denied"), request);

                assertThat(problem.getStatus()).isEqualTo(403);
                assertThat(problem.getTitle()).isEqualTo("ACCESS_DENIED");
                assertThat(problem.getDetail()).isEqualTo("접근 권한이 없습니다.");
                assertThat(problem.getProperties()).containsEntry("code", "ACCESS_DENIED");
            });
        }

        @Test
        @DisplayName("AuthorizationDeniedException을 403 ProblemDetail로 변환한다")
        void handles_authorization_denied_exception() {
            contextRunner.run(context -> {
                var advice = context.getBean(GlobalRestControllerExceptionAdvice.class);
                var request = new MockHttpServletRequest("GET", "/admin");
                var problem = advice.handleAccessDenied(new AuthorizationDeniedException("denied"), request);

                assertThat(problem.getStatus()).isEqualTo(403);
                assertThat(problem.getTitle()).isEqualTo("ACCESS_DENIED");
                assertThat(problem.getDetail()).isEqualTo("접근 권한이 없습니다.");
                assertThat(problem.getProperties()).containsEntry("code", "ACCESS_DENIED");
            });
        }

        @Test
        @DisplayName("IllegalStateException을 500 ProblemDetail로 변환한다")
        void handles_illegal_state_exception() {
            contextRunner.run(context -> {
                var advice = context.getBean(GlobalRestControllerExceptionAdvice.class);
                var request = new MockHttpServletRequest("POST", "/commands");
                var problem = advice.handleIllegalState(new IllegalStateException("invalid state"), request);

                assertThat(problem.getStatus()).isEqualTo(500);
                assertThat(problem.getTitle()).isEqualTo("ILLEGAL_STATE");
                assertThat(problem.getDetail()).isEqualTo("서버 상태가 올바르지 않습니다.");
                assertThat(problem.getProperties()).containsEntry("code", "ILLEGAL_STATE");
            });
        }

        @Test
        @DisplayName("처리되지 않은 Exception을 fallback 500 ProblemDetail로 변환한다")
        void handles_fallback_exception() {
            contextRunner.run(context -> {
                var advice = context.getBean(GlobalRestControllerExceptionAdvice.class);
                var request = new MockHttpServletRequest("GET", "/unknown");
                var problem = advice.handleFallback(new RuntimeException("boom"), request);

                assertThat(problem.getStatus()).isEqualTo(500);
                assertThat(problem.getTitle()).isEqualTo("INTERNAL_SERVER_ERROR");
                assertThat(problem.getDetail()).isEqualTo("예상하지 못한 오류가 발생했습니다.");
                assertThat(problem.getProperties()).containsEntry("code", "INTERNAL_SERVER_ERROR");
            });
        }

        @Test
        @DisplayName("typeBaseUri 설정으로 problem type URI base를 구성한다")
        void configures_problem_type_base_uri_from_property() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.webmvc.error.type-base-uri=https://api.example.test/problems")
                    .run(context -> {
                        var factory = context.getBean(ProblemDetailFactory.class);
                        var problem = factory.build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다.");

                        assertThat(problem.getType().toString())
                                .isEqualTo("https://api.example.test/problems/BAD_REQUEST");
                    });
        }

        @Test
        @DisplayName("사용자 정의 bean이 있으면 기본 bean으로 덮어쓰지 않는다")
        void does_not_override_existing_beans() {
            HttpStatusResolver customStatusResolver = errorCode -> org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

            contextRunner
                    .withBean(HttpStatusResolver.class, () -> customStatusResolver)
                    .run(context -> assertThat(context.getBean(HttpStatusResolver.class)).isSameAs(customStatusResolver));
        }

        @Test
        @DisplayName("capstone.bootstrap.webmvc.enabled가 false이면 자동 설정하지 않는다")
        void does_not_register_beans_when_webmvc_starter_is_disabled() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.webmvc.enabled=false")
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(GlobalRestControllerExceptionAdvice.class);
                        assertThat(context).doesNotHaveBean(ProblemDetailFactory.class);
                    });
        }
    }

    private enum TestErrorCode implements ApplicationErrorCode {
        TIMELINE_NOT_FOUND("A001", "타임라인을 찾을 수 없습니다.");

        private final String code;
        private final String message;

        TestErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
