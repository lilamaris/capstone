package com.lilamaris.capstone.identity.auth.security.shared.config;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.contract.IdentityAuthNamespace;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import com.lilamaris.capstone.kernel.core.namespace.RunningNamespaceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("IdentityAuthSecurityConfiguration 테스트")
class IdentityAuthSecurityConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(IdentityAuthSecurityConfiguration.class)
            .withBean(IssueJwtUseCase.class, () -> mock(IssueJwtUseCase.class))
            .withBean(IssueOpaqueTokenUseCase.class, () -> mock(IssueOpaqueTokenUseCase.class))
            .withBean(ObjectMapper.class, ObjectMapper::new);

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("security shared 기본 bean을 등록한다")
        void register_default_shared_security_beans() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(RunningNamespaceContext.class);
                assertThat(context.getBean(RunningNamespaceContext.class).get())
                        .isEqualTo(IdentityAuthNamespace.NAMESPACE);
                assertThat(context).hasSingleBean(TokenResponseProcessor.class);
                assertThat(context).hasSingleBean(ResponseWriter.class);
                assertThat(context).hasSingleBean(NamespaceRoleSerializer.class);
            });
        }

        @Test
        @DisplayName("JwksReader가 있으면 내부 key store 기반 JwtDecoder를 등록한다")
        void register_jwt_decoder_when_jwks_reader_is_available() {
            contextRunner
                    .withBean(JwksReader.class, () -> mock(JwksReader.class))
                    .run(context -> {
                        assertThat(context).hasSingleBean(JwtDecoder.class);
                        assertThat(context.getBean(JwtDecoder.class))
                                .isInstanceOf(NimbusJwtDecoder.class);
                    });
        }

        @Test
        @DisplayName("이미 등록된 JwtDecoder가 있으면 기본 JwtDecoder로 덮어쓰지 않는다")
        void does_not_override_existing_jwt_decoder() {
            JwtDecoder customJwtDecoder = token -> null;

            contextRunner
                    .withBean(JwksReader.class, () -> mock(JwksReader.class))
                    .withBean(JwtDecoder.class, () -> customJwtDecoder)
                    .run(context -> assertThat(context.getBean(JwtDecoder.class))
                            .isSameAs(customJwtDecoder));
        }

        @Test
        @DisplayName("JwksReader가 없으면 JwtDecoder를 등록하지 않는다")
        void does_not_register_jwt_decoder_without_jwks_reader() {
            contextRunner.run(context -> assertThat(context).doesNotHaveBean(JwtDecoder.class));
        }
    }
}
