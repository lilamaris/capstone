package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.identity.core.actor.CanonicalRole;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespaceFixture;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.Clock;
import java.time.Instant;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserGrantedRole 테스트")
public class UserGrantedRoleTest {
    Clock clock = FixedClock.getFixed();

    Instant now = clock.instant();

    User user = UserFixture.createUser(now);
    ApplicationNamespace namespace = ApplicationNamespaceFixture.createApplicationNamespace();

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("user, namespace, role, createdAt으로 생성한다")
        void create_with_user_namespace_role_and_created_at() {
            var role = CanonicalRole.USER;

            var grantedRole = UserGrantedRole.of(user, namespace, role, now);

            assertThat(grantedRole.getUser()).isSameAs(user);
            assertThat(grantedRole.getNamespace().name()).isEqualTo(namespace.name());
            assertThat(grantedRole.getRole()).isEqualTo(role);
            assertThat(grantedRole.getCreatedAt()).isEqualTo(now);
        }

        @ParameterizedTest(name = "role = {0}")
        @EnumSource(CanonicalRole.class)
        @DisplayName("모든 CanonicalRole로 생성 가능")
        void create_with_all_canonical_roles(CanonicalRole role) {
            var grantedRole = UserGrantedRole.of(user, namespace, role, now);

            assertThat(grantedRole.getRole()).isEqualTo(role);
        }

        @Test
        @DisplayName("다른 ApplicationNamespace 구현체로 생성 가능")
        void create_with_other_application_namespace_implementation() {
            var namespaceName = "identity";
            var role = CanonicalRole.ADMIN;

            ApplicationNamespace namespace = new ApplicationNamespace() {
                @Override
                public String name() {
                    return namespaceName;
                }
            };

            var grantedRole = UserGrantedRole.of(user, namespace, role, now);

            assertThat(grantedRole.getNamespace()).isInstanceOf(EmbeddableApplicationNamespace.class);
            assertThat(grantedRole.getNamespace().name()).isEqualTo(namespaceName);
            assertThat(grantedRole.getRole()).isEqualTo(role);
        }

        @ParameterizedTest(name = "user = {0}")
        @NullSource
        @DisplayName("user가 null이면 예외")
        void throw_exception_when_null_user(User user) {
            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    CanonicalRole.USER,
                    now
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("user");
        }

        @ParameterizedTest(name = "namespace = {0}")
        @NullSource
        @DisplayName("namespace가 null이면 예외")
        void throw_exception_when_null_namespace(ApplicationNamespace namespace) {
            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    CanonicalRole.USER,
                    now
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespace");
        }

        @Test
        @DisplayName("namespace의 name이 빈 문자열이면 예외")
        void throw_exception_when_namespace_name_is_blank() {
            ApplicationNamespace namespace = new ApplicationNamespace() {
                @Override
                public String name() {
                    return " ";
                }
            };

            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    CanonicalRole.USER,
                    now
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("name");
        }

        @Test
        @DisplayName("namespace의 name이 null이면 예외")
        void throw_exception_when_namespace_name_is_null() {
            ApplicationNamespace namespace = new ApplicationNamespace() {
                @Override
                public String name() {
                    return null;
                }
            };

            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    CanonicalRole.USER,
                    now
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("name");
        }

        @ParameterizedTest(name = "role = {0}")
        @NullSource
        @DisplayName("role이 null이면 예외")
        void throw_exception_when_null_role(CanonicalRole role) {
            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    role,
                    now
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("role");
        }

        @ParameterizedTest(name = "createdAt = {0}")
        @NullSource
        @DisplayName("createdAt이 null이면 예외")
        void throw_exception_when_null_created_at(Instant createdAt) {
            assertThatDomainThrownBy(() -> UserGrantedRole.of(
                    user,
                    namespace,
                    CanonicalRole.USER,
                    createdAt
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("createdAt");
        }
    }
}
