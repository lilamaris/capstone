package com.lilamaris.capstone.identity.auth.security.credential.config;

import com.lilamaris.capstone.bootstrap.security.customizer.AuthorizeHttpRequestCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.HttpSecurityCustomizer;
import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.RegisterCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.security.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.capstone.identity.auth.security.credential.filter.JsonCredentialSignUpProcessingFilter;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("CredentialSecurityConfiguration 테스트")
class CredentialSecurityConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CredentialSecurityConfiguration.class)
            .withBean(AuthenticateCredentialAccountUseCase.class, () -> mock(AuthenticateCredentialAccountUseCase.class))
            .withBean(RegisterCredentialAccountUseCase.class, () -> mock(RegisterCredentialAccountUseCase.class))
            .withBean(TokenResponseProcessor.class, () -> mock(TokenResponseProcessor.class))
            .withBean(ResponseWriter.class, () -> mock(ResponseWriter.class))
            .withBean(NamespaceRoleSerializer.class, SeparatorBasedNamespaceRoleSerializer::new)
            .withBean(ObjectMapper.class, ObjectMapper::new);

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("credential 인증 진입점을 bootstrap customizer로 등록한다")
        void register_credential_authentication_customizers() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(AuthorizeHttpRequestCustomizer.class);
                assertThat(context).hasSingleBean(HttpSecurityCustomizer.class);
                assertThat(context).doesNotHaveBean(SecurityFilterChain.class);
            });
        }

        @Test
        @DisplayName("credential 인증 필터와 AuthenticationManager를 등록한다")
        void register_credential_authentication_filters_and_manager() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(JsonCredentialSignInProcessingFilter.class);
                assertThat(context).hasSingleBean(JsonCredentialSignUpProcessingFilter.class);
                assertThat(context).hasSingleBean(AuthenticationManager.class);
            });
        }
    }
}
