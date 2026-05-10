package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.actor.Capability;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.lilamaris.capstone.identity.core.TestSupport.*;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InMemoryRoleCapabilityRegistry 테스트")
public class InMemoryRoleCapabilityRegistryTest extends RoleCapabilityResolverContractTest {

    @Override
    protected RoleCapabilityResolver createResolver() {
        return new InMemoryRoleCapabilityRegistry(List.of(
                guestRoleCapability,
                userRoleCapability,
                maintainerRoleCapability,
                adminRoleCapability
        ));
    }

    @Override
    protected Capability userCapability() {
        return userCapability;
    }

    @Override
    protected Capability maintainerCapability() {
        return maintainerCapability;
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("roleCapabilities로 생성한다")
        void create_with_role_capabilities() {
            var registry = new InMemoryRoleCapabilityRegistry(List.of(
                    guestRoleCapability,
                    userRoleCapability
            ));

            assertThat(registry.resolve(CanonicalRole.GUEST))
                    .containsExactly(guestCapability);
        }

        @ParameterizedTest(name = "roleCapabilities = {0}")
        @NullSource
        @DisplayName("roleCapabilities가 null이면 예외")
        void throw_exception_when_null_role_capabilities(List<RoleCapabilities> roleCapabilities) {
            assertThatDomainThrownBy(() -> new InMemoryRoleCapabilityRegistry(roleCapabilities))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("roleCapabilities");
        }

        @Test
        @DisplayName("같은 role의 capability가 중복 등록되면 예외")
        void throw_exception_when_duplicated_role_capabilities_exist() {
            assertThatDomainThrownBy(() -> new InMemoryRoleCapabilityRegistry(List.of(
                    RoleCapabilities.of(CanonicalRole.USER, Set.of(userCapability)),
                    RoleCapabilities.of(CanonicalRole.USER, Set.of(adminCapability))
            )))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated role capabilities. role=USER");
        }

        @Test
        @DisplayName("생성 후 원본 capability 목록이 변경되어도 registry에 반영되지 않는다")
        void does_not_reflect_source_capabilities_change_after_creation() {
            var capabilities = new HashSet<Capability>();
            capabilities.add(userCapability);

            var registry = new InMemoryRoleCapabilityRegistry(List.of(
                    RoleCapabilities.of(CanonicalRole.USER, capabilities)
            ));
            capabilities.add(adminCapability);

            assertThat(registry.resolve(CanonicalRole.USER))
                    .containsExactly(userCapability);
        }
    }

    @Nested
    @DisplayName("조회 테스트")
    class ResolveTest {
        @Test
        @DisplayName("상위 role은 하위 role의 capability를 포함한다")
        void resolve_capabilities_with_lower_role_capabilities() {
            var registry = createResolver();

            var capabilities = registry.resolve(CanonicalRole.ADMIN);

            assertThat(capabilities)
                    .containsExactlyInAnyOrder(
                            guestCapability,
                            userCapability,
                            maintainerCapability,
                            adminCapability
                    );
        }

        @Test
        @DisplayName("하위 role은 상위 role의 capability를 포함하지 않는다")
        void does_not_resolve_higher_role_capabilities() {
            var registry = createResolver();

            var capabilities = registry.resolve(CanonicalRole.USER);

            assertThat(capabilities)
                    .containsExactlyInAnyOrder(guestCapability, userCapability)
                    .doesNotContain(maintainerCapability, adminCapability);
        }

        @Test
        @DisplayName("등록되지 않은 role capability는 빈 목록으로 누적한다")
        void resolve_with_empty_capabilities_when_role_capabilities_are_not_registered() {
            var registry = new InMemoryRoleCapabilityRegistry(List.of(
                    RoleCapabilities.of(CanonicalRole.ADMIN, Set.of(adminCapability))
            ));

            assertThat(registry.resolve(CanonicalRole.MAINTAINER)).isEmpty();
            assertThat(registry.resolve(CanonicalRole.ADMIN)).containsExactly(adminCapability);
        }

        @Test
        @DisplayName("조회 결과는 변경할 수 없다")
        void resolved_capabilities_are_immutable() {
            var registry = createResolver();
            var capabilities = registry.resolve(CanonicalRole.USER);

            assertThatDomainThrownBy(() -> capabilities.add(adminCapability))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
