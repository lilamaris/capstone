package com.lilamaris.capstone.bootstrap.security;

import com.lilamaris.capstone.bootstrap.security.customizer.AuthorizeHttpRequestCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.HttpSecurityCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.OAuth2ResourceServerCustomizer;
import com.lilamaris.capstone.identity.client.IdentityClientAutoConfigure;
import com.lilamaris.capstone.identity.client.jwt.ActorAuthenticationConverter;
import com.lilamaris.capstone.identity.client.jwt.ActorContextBindingFilter;
import com.lilamaris.capstone.kernel.core.namespace.RunningNamespaceContext;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurityStarterAutoConfigure 테스트")
class SecurityStarterAutoConfigureTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    IdentityClientAutoConfigure.class,
                    SecurityStarterAutoConfigure.class
            ))
            .withPropertyValues("identity.client.jwks-uri=https://identity.example.test/.well-known/jwks.json")
            .withBean(RunningNamespaceContext.class, () -> () -> SimpleApplicationNamespace.of("timeline"));

    private final WebApplicationContextRunner securityOnlyContextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SecurityStarterAutoConfigure.class));

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("identity security 기본 bean을 등록한다")
        void register_default_identity_security_beans() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(SecurityFilterChain.class);
                assertThat(context).hasSingleBean(CorsConfigurationSource.class);
                assertThat(context).hasSingleBean(JwtDecoder.class);
                assertThat(context).hasSingleBean(ActorAuthenticationConverter.class);
                assertThat(context).hasSingleBean(ActorContextBindingFilter.class);
                assertThat(context).hasBean("authorizeHttpRequestsCustomizer");
                assertThat(context).hasBean("oAuth2ResourceServerCustomizer");
                assertThat(context).hasBean("actorContextBindingFilterCustomizer");
                assertThat(context).hasBean("jwtResourceServerCustomizer");
                assertThat(context).hasBean("permitAllRequestsCustomizer");
            });
        }

        @Test
        @DisplayName("identity client bean이 없어도 기본 security chain을 등록한다")
        void register_default_security_filter_chain_without_identity_client_beans() {
            securityOnlyContextRunner.run(context -> {
                assertThat(context).hasSingleBean(SecurityFilterChain.class);
                assertThat(context).hasSingleBean(CorsConfigurationSource.class);
                assertThat(context).hasBean("authorizeHttpRequestsCustomizer");
                assertThat(context).hasBean("oAuth2ResourceServerCustomizer");
                assertThat(context).hasBean("permitAllRequestsCustomizer");
                assertThat(context).doesNotHaveBean(OAuth2ResourceServerCustomizer.class);
                assertThat(context).doesNotHaveBean("jwtResourceServerCustomizer");
                assertThat(context).doesNotHaveBean("actorContextBindingFilterCustomizer");
            });
        }

        @Test
        @DisplayName("ActorContextBindingFilter는 서블릿 필터 자동 등록을 비활성화한다")
        void disables_servlet_registration_for_actor_context_binding_filter() {
            contextRunner.run(context -> {
                var filter = context.getBean(ActorContextBindingFilter.class);
                var registration = context.getBean(
                        "actorContextBindingFilterRegistration",
                        FilterRegistrationBean.class
                );

                assertThat(registration.getFilter()).isSameAs(filter);
                assertThat(registration.isEnabled()).isFalse();
            });
        }

        @Test
        @DisplayName("사용자 정의 SecurityFilterChain이 있으면 기본 체인을 등록하지 않는다")
        void does_not_register_default_security_filter_chain_when_existing_chain_is_present() {
            contextRunner
                    .withBean(SecurityFilterChain.class, () -> new SecurityFilterChain() {
                        @Override
                        public boolean matches(HttpServletRequest request) {
                            return false;
                        }

                        @Override
                        public List<Filter> getFilters() {
                            return List.of();
                        }
                    })
                    .run(context -> assertThat(context).hasSingleBean(SecurityFilterChain.class));
        }

        @Test
        @DisplayName("capstone.bootstrap.security.enabled가 false이면 자동 설정하지 않는다")
        void does_not_register_beans_when_security_starter_is_disabled() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.security.enabled=false")
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(SecurityFilterChain.class);
                        assertThat(context).doesNotHaveBean(CorsConfigurationSource.class);
                        assertThat(context).doesNotHaveBean(HttpSecurityCustomizer.class);
                        assertThat(context).doesNotHaveBean(OAuth2ResourceServerCustomizer.class);
                    });
        }

        @Test
        @DisplayName("CORS가 비활성화되면 기본 CorsConfigurationSource를 등록하지 않는다")
        void does_not_register_cors_configuration_source_when_cors_is_disabled() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.security.cors.enabled=false")
                    .run(context -> assertThat(context).doesNotHaveBean(CorsConfigurationSource.class));
        }

        @Test
        @DisplayName("authorize가 비활성화되면 기본 permit/anyRequest customizer를 등록하지 않는다")
        void does_not_register_default_authorize_rule_when_authorize_is_disabled() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.security.authorize.enabled=false")
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(AuthorizeHttpRequestCustomizer.class);
                        assertThat(context).doesNotHaveBean("permitAllRequestsCustomizer");
                    });
        }
    }
}
