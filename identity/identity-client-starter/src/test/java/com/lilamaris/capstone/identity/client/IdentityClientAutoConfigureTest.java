package com.lilamaris.capstone.identity.client;

import com.lilamaris.capstone.identity.client.jwt.ActorAuthenticationConverter;
import com.lilamaris.capstone.identity.client.jwt.ActorContextBindingFilter;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.actor.context.ThreadLocalActorContextHolder;
import com.lilamaris.capstone.identity.core.role.*;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IdentityClientAutoConfigure 테스트")
class IdentityClientAutoConfigureTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(IdentityClientAutoConfigure.class))
            .withPropertyValues("identity.client.jwks-uri=https://identity.example.test/.well-known/jwks.json")
            .withBean(com.lilamaris.capstone.kernel.core.namespace.RunningNamespaceContext.class,
                    () -> () -> SimpleApplicationNamespace.of("timeline"));

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("identity client 기본 bean을 등록한다")
        void register_default_identity_client_beans() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(JwtDecoder.class);
                assertThat(context).hasSingleBean(ActorAuthenticationConverter.class);
                assertThat(context).hasSingleBean(ActorContextBindingFilter.class);
                assertThat(context).hasSingleBean(ActorContextHolder.class);
                assertThat(context).hasSingleBean(RoleCapabilityResolver.class);
                assertThat(context).hasSingleBean(NamespaceRoleSerializer.class);
                assertThat(context).hasSingleBean(NamespaceRoleDeserializer.class);
            });
        }

        @Test
        @DisplayName("이미 등록된 bean이 있으면 기본 bean으로 덮어쓰지 않는다")
        void does_not_override_existing_beans() {
            var customActorContextHolder = new ThreadLocalActorContextHolder();

            contextRunner
                    .withBean(ActorContextHolder.class, () -> customActorContextHolder)
                    .run(context -> assertThat(context.getBean(ActorContextHolder.class))
                            .isSameAs(customActorContextHolder));
        }

        @Test
        @DisplayName("identity.client.enabled가 false이면 자동 설정하지 않는다")
        void does_not_register_beans_when_identity_client_is_disabled() {
            contextRunner
                    .withPropertyValues("identity.client.enabled=false")
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(JwtDecoder.class);
                        assertThat(context).doesNotHaveBean(ActorAuthenticationConverter.class);
                        assertThat(context).doesNotHaveBean(ActorContextBindingFilter.class);
                    });
        }
    }
}
