package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.actor.Capability;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class RoleCapabilityResolverContractTest {
    protected abstract RoleCapabilityResolver createResolver();

    protected abstract Capability userCapability();

    protected abstract Capability maintainerCapability();

    @Test
    @DisplayName("role의 capability를 조회한다")
    void resolve_role_capabilities() {
        var resolver = createResolver();

        var capabilities = resolver.resolve(CanonicalRole.USER);

        assertThat(capabilities).contains(userCapability());
    }

    @ParameterizedTest(name = "role = {0}")
    @NullSource
    @DisplayName("role이 null이면 예외")
    void throw_exception_when_null_role(CanonicalRole role) {
        var resolver = createResolver();

        assertThatDomainThrownBy(() -> resolver.resolve(role))
                .isInstanceOf(NullPointerException.class)
                .hasNonNullMessageFor("role");
    }

    @Test
    @DisplayName("여러 role의 capability를 합쳐서 조회한다")
    void resolve_role_capabilities_union() {
        var resolver = createResolver();

        var capabilities = resolver.resolve(List.of(CanonicalRole.USER, CanonicalRole.MAINTAINER));

        assertThat(capabilities)
                .contains(userCapability(), maintainerCapability());
    }

    @Test
    @DisplayName("중복 role이 있어도 capability는 중복 없이 조회한다")
    void resolve_capabilities_distinctly_when_roles_are_duplicated() {
        var resolver = createResolver();

        var capabilities = resolver.resolve(List.of(CanonicalRole.USER, CanonicalRole.USER));

        assertThat(capabilities)
                .contains(userCapability())
                .doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("role이 비어있으면 빈 capability를 조회한다")
    void resolve_empty_capabilities_when_roles_are_empty() {
        var resolver = createResolver();

        var capabilities = resolver.resolve(Set.of());

        assertThat(capabilities).isEmpty();
    }

    @ParameterizedTest(name = "roles = {0}")
    @NullSource
    @DisplayName("roles가 null이면 예외")
    void throw_exception_when_null_roles(Collection<CanonicalRole> roles) {
        var resolver = createResolver();

        assertThatDomainThrownBy(() -> resolver.resolve(roles))
                .isInstanceOf(NullPointerException.class)
                .hasNonNullMessageFor("roles");
    }
}
