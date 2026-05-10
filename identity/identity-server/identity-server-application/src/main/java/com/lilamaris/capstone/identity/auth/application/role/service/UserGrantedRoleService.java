package com.lilamaris.capstone.identity.auth.application.role.service;

import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.role.port.in.GrantRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.ListUserGrantedRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.RevokeRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.command.GrantRoleCommand;
import com.lilamaris.capstone.identity.auth.application.role.port.in.command.RevokeRoleCommand;
import com.lilamaris.capstone.identity.auth.application.role.port.in.query.ListUserGrantedRoleQuery;
import com.lilamaris.capstone.identity.auth.application.role.port.in.result.UserGrantedRoleResult;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleStore;
import com.lilamaris.capstone.identity.auth.application.role.port.out.criteria.UserGrantRoleLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.shared.IdentityAuthCapability;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGrantedRoleService implements
        GrantRoleUseCase,
        RevokeRoleUseCase,
        ListUserGrantedRoleUseCase {

    private final UserGrantedRoleReader reader;
    private final UserGrantedRoleStore store;
    private final UserReader userReader;
    private final Clock clock;

    @Override
    @Transactional
    public UserGrantedRoleResult grant(GrantRoleCommand command) {
        var requester = command.requester();

        if (!requester.capabilities().contains(IdentityAuthCapability.GRANT_ROLE))
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.UNAUTHORIZED_GRANT_ATTEMPT);

        var userId = command.userId();
        var userExists = userReader.existsById(userId);
        if (!userExists) throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND);

        var namespace = SimpleApplicationNamespace.of(command.namespaceName());
        var role = command.role();

        var criteria = UserGrantRoleLookupCriteria.of(userId, namespace, role);
        var grantExists = reader.existsByCriteria(criteria);
        if (grantExists)
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_GRANT_ALREADY_EXISTS);

        var now = clock.instant();

        var userGrantedRole = UserGrantedRole.of(userId, namespace, role, now);

        store.save(userGrantedRole);

        return UserGrantedRoleResult.from(userGrantedRole);
    }

    @Override
    @Transactional
    public void revoke(RevokeRoleCommand command) {
        var requester = command.requester();

        if (!requester.capabilities().contains(IdentityAuthCapability.REVOKE_ROLE))
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.UNAUTHORIZED_REVOKE_ATTEMPT);

        var userId = command.userId();
        var userExists = userReader.existsById(userId);
        if (!userExists) throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND);

        var namespace = SimpleApplicationNamespace.of(command.namespaceName());
        var role = command.role();

        var criteria = UserGrantRoleLookupCriteria.of(userId, namespace, role);
        var userGrantRole = reader.findByCriteria(criteria)
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_GRANT_NOT_FOUND));

        store.delete(userGrantRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGrantedRoleResult> list(ListUserGrantedRoleQuery query) {
        var userId = query.userId();
        var userExists = userReader.existsById(userId);
        if (!userExists) throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND);

        return reader.findByUserId(userId).stream()
                .map(UserGrantedRoleResult::from)
                .toList();
    }
}
