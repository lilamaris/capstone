package com.lilamaris.capstone.identity.client.jwt;

import com.lilamaris.capstone.identity.core.actor.SimpleCapability;
import com.lilamaris.capstone.identity.core.role.*;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Set;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ActorAuthenticationConverter 테스트")
class ActorAuthenticationConverterTest {
    private final SimpleCapability guestCapability = SimpleCapability.of("guest:read", "guest read");
    private final SimpleCapability userCapability = SimpleCapability.of("user:read", "user read");
    private final SimpleCapability adminCapability = SimpleCapability.of("admin:write", "admin write");

    private final ActorAuthenticationConverter converter = new ActorAuthenticationConverter(
            new SeparatorBasedNamespaceRoleDeserializer(),
            () -> SimpleApplicationNamespace.of("timeline"),
            new InMemoryRoleCapabilityRegistry(List.of(
                    RoleCapabilities.of(CanonicalRole.GUEST, Set.of(guestCapability)),
                    RoleCapabilities.of(CanonicalRole.USER, Set.of(userCapability)),
                    RoleCapabilities.of(CanonicalRole.ADMIN, Set.of(adminCapability))
            ))
    );

    @Nested
    @DisplayName("변환 테스트")
    class ConvertTest {
        @Test
        @DisplayName("현재 namespace의 role을 actor capability와 authority로 변환한다")
        void convert_current_namespace_roles_to_actor_capabilities_and_authorities() {
            // given
            var jwt = jwt("user-1", Set.of("timeline:USER", "identity:ADMIN"));

            // when
            var authentication = converter.convert(jwt);

            // then
            assertThat(authentication).isInstanceOf(ActorAuthenticationToken.class);
            assertThat(authentication.isAuthenticated()).isTrue();
            assertThat(authentication.getPrincipal())
                    .isInstanceOfSatisfying(com.lilamaris.capstone.identity.core.actor.Actor.class, actor -> {
                        assertThat(actor.subject()).isEqualTo("user-1");
                        assertThat(actor.capabilities()).containsExactlyInAnyOrder(guestCapability, userCapability);
                    });
            assertThat(authentication.getAuthorities())
                    .extracting("authority")
                    .containsExactlyInAnyOrder("ROLE_USER", "guest:read", "user:read");
        }

        @Test
        @DisplayName("현재 namespace에 해당하는 role이 없으면 capability와 authority가 비어 있다")
        void convert_to_empty_capabilities_when_current_namespace_roles_do_not_exist() {
            // given
            var jwt = jwt("user-1", Set.of("identity:ADMIN"));

            // when
            var authentication = converter.convert(jwt);

            // then
            assertThat(authentication.getPrincipal())
                    .isInstanceOfSatisfying(com.lilamaris.capstone.identity.core.actor.Actor.class, actor ->
                            assertThat(actor.capabilities()).isEmpty()
                    );
            assertThat(authentication.getAuthorities()).isEmpty();
        }

        @Test
        @DisplayName("scopes claim이 없으면 빈 capability를 가진 actor로 변환한다")
        void convert_with_empty_capabilities_when_scopes_claim_is_missing() {
            // given
            var jwt = Jwt.withTokenValue("access-token")
                    .header("alg", "none")
                    .subject("user-1")
                    .build();

            // when
            var authentication = converter.convert(jwt);

            // then
            assertThat(authentication.getPrincipal())
                    .isInstanceOfSatisfying(com.lilamaris.capstone.identity.core.actor.Actor.class, actor ->
                            assertThat(actor.capabilities()).isEmpty()
                    );
        }

        @Test
        @DisplayName("source가 null이면 예외")
        void throw_exception_when_null_source() {
            assertThatDomainThrownBy(() -> converter.convert(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("source");
        }
    }

    private Jwt jwt(String subject, Set<String> scopes) {
        return Jwt.withTokenValue("access-token")
                .header("alg", "none")
                .subject(subject)
                .claim("scopes", scopes)
                .build();
    }
}
