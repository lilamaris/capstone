package com.lilamaris.capstone.identity.auth.application.role;

import com.lilamaris.capstone.identity.auth.application.role.port.in.command.GrantRoleCommand;
import com.lilamaris.capstone.identity.auth.application.role.port.in.command.RevokeRoleCommand;
import com.lilamaris.capstone.identity.auth.application.role.port.in.query.ListUserGrantedRoleQuery;
import com.lilamaris.capstone.identity.auth.application.shared.config.IdentityAuthCapability;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.Capability;
import com.lilamaris.capstone.identity.core.actor.SimpleActor;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import com.lilamaris.capstone.kernel.testsupport.generator.SequenceCounter;
import com.lilamaris.capstone.kernel.testsupport.generator.UuidGenerator;

import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class RoleUseCaseTestSupport {
    public static final Clock CLOCK = FixedClock.getFixed();
    public static final Instant NOW = CLOCK.instant();

    public static final UUID USER_ID = new UuidGenerator(new SequenceCounter(1)).generate();
    public static final String NAMESPACE_NAME = "identity-auth";
    public static final CanonicalRole ROLE = CanonicalRole.USER;

    private RoleUseCaseTestSupport() {
    }

    public static UserGrantedRole userGrantedRole() {
        return UserGrantedRole.of(
                USER_ID,
                SimpleApplicationNamespace.of(NAMESPACE_NAME),
                ROLE,
                NOW
        );
    }

    public static GrantRoleCommand grantRoleCommand() {
        return new GrantRoleCommand(USER_ID, NAMESPACE_NAME, ROLE, actor(IdentityAuthCapability.GRANT_ROLE));
    }

    public static GrantRoleCommand unauthorizedGrantRoleCommand() {
        return new GrantRoleCommand(USER_ID, NAMESPACE_NAME, ROLE, actor());
    }

    public static RevokeRoleCommand revokeRoleCommand() {
        return new RevokeRoleCommand(USER_ID, NAMESPACE_NAME, ROLE, actor(IdentityAuthCapability.REVOKE_ROLE));
    }

    public static RevokeRoleCommand unauthorizedRevokeRoleCommand() {
        return new RevokeRoleCommand(USER_ID, NAMESPACE_NAME, ROLE, actor());
    }

    public static ListUserGrantedRoleQuery listUserGrantedRoleQuery() {
        return new ListUserGrantedRoleQuery(USER_ID);
    }

    public static Actor actor(Capability... capabilities) {
        return SimpleActor.of("tester", Set.of(capabilities));
    }

    public static void assertApplicationError(
            Runnable action,
            IdentityAuthApplicationErrorCode errorCode
    ) {
        assertThatThrownBy(action::run)
                .isInstanceOfSatisfying(IdentityAuthApplicationException.class, exception ->
                        org.assertj.core.api.Assertions.assertThat(exception.getErrorCode()).isEqualTo(errorCode)
                );
    }
}
