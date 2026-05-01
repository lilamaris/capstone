package com.lilamaris.capstone.identity.auth.application.account.service;

import com.lilamaris.capstone.identity.auth.application.account.model.UserAccountProvisioner;
import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.LinkFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.ListFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.UnlinkFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.LinkFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.UnlinkFederatedAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.query.ListFederatedAccountQuery;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.FederatedAccountResult;
import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedProviderLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedUserLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.role.internal.InitialUserGrantedRoleProvisioner;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FederatedAccountService implements
        AuthenticateFederatedAccountUseCase,
        LinkFederatedAccountUseCase,
        UnlinkFederatedAccountUseCase,
        ListFederatedAccountUseCase {

    private final FederatedAccountReader reader;
    private final FederatedAccountStore store;
    private final UserReader userReader;
    private final UserStore userStore;
    private final UserGrantedRoleReader userGrantedRoleReader;

    private final UserAccountProvisioner accountProvisioner;
    private final InitialUserGrantedRoleProvisioner initialUserGrantedRoleProvisioner;
    private final Clock clock;

    @Override
    @Transactional
    public AuthenticationResult authenticate(AuthenticateFederatedAccountCommand command) {
        ensureNotExistsAccount(command.registrationId(), command.providerUserId());

        var now = clock.instant();
        var provisioned = accountProvisioner.createFederatedUser(
                command.providerUserNickname(),
                command.registrationId(),
                command.providerUserId(),
                now
        );

        var user = provisioned.user();
        var account = provisioned.account();

        var savedUser = userStore.save(user);
        store.save(account);

        var userGrantedRoles = initialUserGrantedRoleProvisioner.grant(savedUser.getId());

        return AuthenticationResult.from(user, userGrantedRoles);
    }

    @Override
    @Transactional
    public AuthenticationResult link(LinkFederatedAccountCommand command) {
        ensureNotExistsAccount(command.registrationId(), command.providerUserId());

        var userId = command.userId();
        var user = userReader.findById(userId)
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND));

        var now = clock.instant();
        var provisioned = accountProvisioner.linkFederated(user, command.registrationId(), command.providerUserId(), now);

        store.save(provisioned.account());

        var userGrantedRoles = userGrantedRoleReader.findByUserId(userId);

        return AuthenticationResult.from(user, userGrantedRoles);
    }

    @Override
    @Transactional
    public void unlink(UnlinkFederatedAccountCommand command) {
        var criteria = FederatedUserLookupCriteria.of(command.userId(), command.registrationId());
        var account = reader.findByCriteria(criteria)
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND));

        store.delete(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FederatedAccountResult> list(ListFederatedAccountQuery command) {
        var exists = userReader.existsById(command.userId());
        if (!exists) throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND);

        return reader.findByUserId(command.userId()).stream()
                .map(FederatedAccountResult::from)
                .toList();
    }

    private void ensureNotExistsAccount(String registrationId, String providerUserId) {
        var criteria = FederatedProviderLookupCriteria.of(registrationId, providerUserId);
        var exists = reader.existsByCriteria(criteria);
        if (exists) throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.ACCOUNT_ALREADY_EXISTS);
    }
}
