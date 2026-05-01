package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SeparatorBasedNamespaceRoleSerializer 테스트")
public class SeparatorBasedNamespaceRoleSerializerTest {
    private final SeparatorBasedNamespaceRoleSerializer serializer = new SeparatorBasedNamespaceRoleSerializer();

    @Nested
    @DisplayName("직렬화 테스트")
    class SerializationTest {
        @Test
        @DisplayName("namespaceRole을 namespace와 role을 구분자로 연결한 문자열로 직렬화한다")
        void serialize_namespace_role() {
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);

            var serialized = serializer.serialize(namespaceRole);

            assertThat(serialized).isEqualTo("identity:ADMIN");
        }

        @Test
        @DisplayName("namespaceRole 컬렉션을 문자열 집합으로 직렬화한다")
        void serialize_namespace_roles() {
            var identityRole = NamespaceRoleFixture.createNamespaceRole("identity", CanonicalRole.ADMIN);
            var reservationRole = NamespaceRoleFixture.createNamespaceRole("reservation", CanonicalRole.USER);

            var serialized = serializer.serialize(List.of(identityRole, reservationRole));

            assertThat(serialized)
                    .containsExactlyInAnyOrder("identity:ADMIN", "reservation:USER");
        }

        @ParameterizedTest(name = "namespaceRole = {0}")
        @NullSource
        @DisplayName("namespaceRole이 null이면 예외")
        void throw_exception_when_null_namespace_role(NamespaceRole namespaceRole) {
            assertThatDomainThrownBy(() -> serializer.serialize(namespaceRole))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("source");
        }

        @ParameterizedTest(name = "namespaceRoles = {0}")
        @NullSource
        @DisplayName("namespaceRole 컬렉션이 null이면 예외")
        void throw_exception_when_null_namespace_roles(List<NamespaceRole> namespaceRoles) {
            assertThatDomainThrownBy(() -> serializer.serialize(namespaceRoles))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("sources");
        }

        @Test
        @DisplayName("namespace에 구분자가 포함되어 있으면 예외")
        void throw_exception_when_namespace_contains_separator() {
            var namespace = TestSupport.createApplicationNamespace("identity:auth");
            var namespaceRole = NamespaceRoleFixture.createNamespaceRole(namespace, CanonicalRole.USER);

            assertThatDomainThrownBy(() -> serializer.serialize(namespaceRole))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("namespace value must not contain separator ':'");
        }
    }
}
