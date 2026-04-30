package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.TestSupport;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SimpleNamespaceRole 테스트")
public class SimpleNamespaceRoleTest {
    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("namespace와 role로 생성한다")
        void create_with_namespace_and_role() {
            var namespace = NamespaceRoleFixture.INITIAL_NAMESPACE;
            var role = CanonicalRole.USER;

            var namespaceRole = SimpleNamespaceRole.of(namespace, role);

            assertThat(namespaceRole.namespace()).isEqualTo(namespace);
            assertThat(namespaceRole.role()).isEqualTo(role);
        }

        @ParameterizedTest(name = "namespace = {0}")
        @NullSource
        @DisplayName("namespace가 null이면 예외")
        void throw_exception_when_null_namespace(ApplicationNamespace namespace) {
            assertThatDomainThrownBy(() -> SimpleNamespaceRole.of(namespace, CanonicalRole.USER))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespace");
        }

        @ParameterizedTest(name = "role = {0}")
        @NullSource
        @DisplayName("role이 null이면 예외")
        void throw_exception_when_null_role(CanonicalRole role) {
            assertThatDomainThrownBy(() -> SimpleNamespaceRole.of(
                    NamespaceRoleFixture.INITIAL_NAMESPACE,
                    role
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("role");
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class ConversionTest {
        @Test
        @DisplayName("다른 NamespaceRole 구현체에서 변환 가능")
        void convert_from_source() {
            var otherNamespace = TestSupport.createApplicationNamespace("reservation");
            var otherRole = CanonicalRole.ADMIN;

            NamespaceRole other = new NamespaceRole() {
                @Override
                public ApplicationNamespace namespace() {
                    return otherNamespace;
                }

                @Override
                public CanonicalRole role() {
                    return otherRole;
                }
            };

            var namespaceRole = SimpleNamespaceRole.from(other);

            assertThat(namespaceRole.namespace()).isEqualTo(otherNamespace);
            assertThat(namespaceRole.role()).isEqualTo(otherRole);
        }

        @ParameterizedTest(name = "namespaceRole = {0}")
        @NullSource
        @DisplayName("변환 시 다른 NamespaceRole이 null이면 예외")
        void throw_exception_when_convert_source_is_null(NamespaceRole namespaceRole) {
            assertThatDomainThrownBy(() -> SimpleNamespaceRole.from(namespaceRole))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespaceRole");
        }
    }

    @Nested
    @DisplayName("동일성 비교 테스트")
    class SameTest {
        @Test
        @DisplayName("namespace와 role이 같으면 같은 namespace role로 판단한다")
        void return_true_when_namespace_and_role_are_same() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.USER);
            var other = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.USER);

            var result = namespaceRole.isSame(other);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("namespace가 다르면 다른 namespace role로 판단한다")
        void return_false_when_namespace_is_different() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.USER);
            var other = NamespaceRoleFixture.createNamespaceRole("reservation", CanonicalRole.USER);

            var result = namespaceRole.isSame(other);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("role이 다르면 다른 namespace role로 판단한다")
        void return_false_when_role_is_different() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.USER);
            var other = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);

            var result = namespaceRole.isSame(other);

            assertThat(result).isFalse();
        }

        @ParameterizedTest(name = "namespaceRole = {0}")
        @NullSource
        @DisplayName("비교 대상 namespaceRole이 null이면 예외")
        void throw_exception_when_compare_target_is_null(NamespaceRole namespaceRole) {
            var source = NamespaceRoleFixture.createNamespaceRole();

            assertThatDomainThrownBy(() -> source.isSame(namespaceRole))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespaceRole");
        }
    }
}
