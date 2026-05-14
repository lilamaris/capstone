package com.lilamaris.capstone.identity.auth.security.federated.config;

import com.lilamaris.capstone.bootstrap.security.customizer.AuthorizeHttpRequestCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.HttpSecurityCustomizer;
import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.security.federated.handler.FederatedAuthenticationFailureHandler;
import com.lilamaris.capstone.identity.auth.security.federated.handler.FederatedAuthenticationSuccessHandler;
import com.lilamaris.capstone.identity.auth.security.federated.registry.FederatedPrincipalMapperRegistry;
import com.lilamaris.capstone.identity.auth.security.federated.service.CustomOAuth2UserService;
import com.lilamaris.capstone.identity.auth.security.federated.service.CustomOidcUserService;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("FederatedSecurityConfiguration 테스트")
class FederatedSecurityConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(FederatedSecurityConfiguration.class)
            .withBean(ClientRegistrationRepository.class, () -> mock(ClientRegistrationRepository.class))
            .withBean(AuthenticateFederatedAccountUseCase.class, () -> mock(AuthenticateFederatedAccountUseCase.class))
            .withBean(FederatedPrincipalMapperRegistry.class, () -> mock(FederatedPrincipalMapperRegistry.class))
            .withBean(TokenResponseProcessor.class, () -> mock(TokenResponseProcessor.class))
            .withBean(ResponseWriter.class, () -> mock(ResponseWriter.class))
            .withBean(NamespaceRoleSerializer.class, SeparatorBasedNamespaceRoleSerializer::new);

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("federated 인증 진입점을 bootstrap customizer로 등록한다")
        void register_federated_authentication_customizers() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(AuthorizeHttpRequestCustomizer.class);
                assertThat(context).hasSingleBean(HttpSecurityCustomizer.class);
                assertThat(context).hasBean("userInfoEndpointConfigCustomizer");
                assertThat(context).hasBean("oAuth2LoginConfigurerCustomizer");
                assertThat(context).doesNotHaveBean(SecurityFilterChain.class);
            });
        }

        @Test
        @DisplayName("federated 인증 handler와 user service를 등록한다")
        void register_federated_authentication_handlers_and_user_services() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(FederatedAuthenticationSuccessHandler.class);
                assertThat(context).hasSingleBean(FederatedAuthenticationFailureHandler.class);
                assertThat(context).hasSingleBean(CustomOidcUserService.class);
                assertThat(context).hasSingleBean(CustomOAuth2UserService.class);
            });
        }

        @Test
        @DisplayName("ClientRegistrationRepository가 없으면 federated bean을 등록하지 않는다")
        void does_not_register_federated_beans_without_client_registration_repository() {
            new ApplicationContextRunner()
                    .withUserConfiguration(FederatedSecurityConfiguration.class)
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(AuthorizeHttpRequestCustomizer.class);
                        assertThat(context).doesNotHaveBean(HttpSecurityCustomizer.class);
                    });
        }
    }
}
