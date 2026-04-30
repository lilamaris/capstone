package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.TestSupport;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProviderBasedInitialUserGrantedRoleRegistry 테스트")
public class ProviderBasedInitialUserGrantedRoleRegistryTest {
    private InitialUserGrantedRoleProvider provider(NamespaceRole namespaceRole) {
        return new InitialUserGrantedRoleProvider() {
            @Override
            public NamespaceRole provide() {
                return namespaceRole;
            }
        };
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("providers와 fallbackRole로 생성한다")
        void create_with_providers_and_fallback_role() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);

            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(namespaceRole)),
                    CanonicalRole.USER
            );

            assertThat(registry.getAll())
                    .containsExactly(namespaceRole);
        }

        @ParameterizedTest(name = "providers = {0}")
        @NullSource
        @DisplayName("providers가 null이면 예외")
        void throw_exception_when_null_providers(List<InitialUserGrantedRoleProvider> providers) {
            assertThatDomainThrownBy(() -> new ProviderBasedInitialUserGrantedRoleRegistry(
                    providers,
                    CanonicalRole.USER
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("providers");
        }

        @ParameterizedTest(name = "fallbackRole = {0}")
        @NullSource
        @DisplayName("fallbackRole이 null이면 예외")
        void throw_exception_when_null_fallback_role(CanonicalRole fallbackRole) {
            assertThatDomainThrownBy(() -> new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(),
                    fallbackRole
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("fallbackRole");
        }

        @Test
        @DisplayName("같은 namespace를 제공하는 provider가 있으면 예외")
        void throw_exception_when_duplicated_namespace_provider_exists() {
            var namespace = TestSupport.createApplicationNamespace("identity");
            var first = NamespaceRoleFixture.builder()
                    .namespace(namespace)
                    .role(CanonicalRole.USER)
                    .build();

            var second = NamespaceRoleFixture.builder()
                    .namespace(namespace)
                    .role(CanonicalRole.ADMIN)
                    .build();

            assertThatDomainThrownBy(() -> new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(first), provider(second)),
                    CanonicalRole.GUEST
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("duplicated namespace found: identity");
        }
    }

    @Nested
    @DisplayName("조회 테스트")
    class ResolveTest {
        @Test
        @DisplayName("등록된 namespace면 provider가 제공한 role을 조회한다")
        void resolve_registered_namespace_role() {
            var namespace = TestSupport.createApplicationNamespace("identity");
            var namespaceRole = NamespaceRoleFixture.builder()
                    .namespace(namespace)
                    .role(CanonicalRole.ADMIN)
                    .build();
            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(namespaceRole)),
                    CanonicalRole.USER
            );

            var resolved = registry.resolveByNamespace(namespace);

            assertThat(resolved).isEqualTo(namespaceRole);
        }

        @Test
        @DisplayName("등록되지 않은 namespace면 fallbackRole로 조회한다")
        void resolve_fallback_role_when_namespace_is_not_registered() {
            var namespace = TestSupport.createApplicationNamespace("reservation");
            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN))),
                    CanonicalRole.USER
            );

            var resolved = registry.resolveByNamespace(namespace);

            assertThat(resolved.namespace()).isEqualTo(namespace);
            assertThat(resolved.role()).isEqualTo(CanonicalRole.USER);
        }

        @Test
        @DisplayName("다른 ApplicationNamespace 구현체라도 name이 같으면 등록된 role을 조회한다")
        void resolve_registered_role_when_other_namespace_implementation_has_same_name() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);
            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(namespaceRole)),
                    CanonicalRole.USER
            );

            ApplicationNamespace namespace = new ApplicationNamespace() {
                @Override
                public String name() {
                    return "identity";
                }
            };

            var resolved = registry.resolveByNamespace(namespace);

            assertThat(resolved).isEqualTo(namespaceRole);
        }

        @ParameterizedTest(name = "namespace = {0}")
        @NullSource
        @DisplayName("namespace가 null이면 예외")
        void throw_exception_when_null_namespace(ApplicationNamespace namespace) {
            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(),
                    CanonicalRole.USER
            );

            assertThatDomainThrownBy(() -> registry.resolveByNamespace(namespace))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespace");
        }
    }

    @Nested
    @DisplayName("전체 조회 테스트")
    class GetAllTest {
        @Test
        @DisplayName("provider가 제공한 namespace role을 모두 조회한다")
        void get_all_namespace_roles() {
            var identityRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);
            var reservationRole = NamespaceRoleFixture.createNamespaceRole("reservation", CanonicalRole.USER);
            var registry = new ProviderBasedInitialUserGrantedRoleRegistry(
                    List.of(provider(identityRole), provider(reservationRole)),
                    CanonicalRole.GUEST
            );

            var namespaceRoles = registry.getAll();

            assertThat(namespaceRoles)
                    .containsExactlyInAnyOrder(identityRole, reservationRole);
        }
    }
}
