package com.lilamaris.capstone.identity.auth.application.account.service;

import com.lilamaris.capstone.identity.auth.application.account.model.UserAccountProvisioner;
import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.ChangeCredentialPasswordUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.RegisterCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.ChangeCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.RegisterCredentialAccountCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;
import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.role.internal.InitialUserGrantedRoleProvisioner;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CredentialAccountService implements
        RegisterCredentialAccountUseCase,
        AuthenticateCredentialAccountUseCase,
        ChangeCredentialPasswordUseCase {

    private final CredentialAccountReader reader;
    private final CredentialAccountStore store;
    private final UserStore userStore;

    private final UserAccountProvisioner accountProvisioner;
    private final InitialUserGrantedRoleProvisioner roleProvisioner;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    @Transactional
    public UserResult register(RegisterCredentialAccountCommand command) {
        var exists = reader.existsByEmail(command.email());
        if (exists)
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.CREDENTIAL_EMAIL_DUPLICATED);

        var now = clock.instant();
        var passwordHash = passwordEncoder.encode(command.password());
        var provisioned = accountProvisioner.createCredentialUser(command.nickname(), command.email(), passwordHash, now);

        var user = provisioned.user();
        var account = provisioned.account();

        var savedUser = userStore.save(user);
        store.save(account);

        roleProvisioner.grant(savedUser.getId());

        return UserResult.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResult authenticate(AuthenticateCredentialAccountCommand command) {
        var account = reader.findByEmail(command.email())
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED));

        var matches = passwordEncoder.matches(command.password(), account.getPasswordHash());
        if (!matches)
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED);

        var user = account.getUser();

        return UserResult.from(user);
    }

    @Override
    @Transactional
    public UserResult change(ChangeCredentialAccountCommand command) {
        var account = reader.findByUserId(command.userId())
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND));

        var matches = passwordEncoder.matches(command.oldPassword(), account.getPasswordHash());
        if (!matches)
            throw new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED);

        account.updatePasswordHash(passwordEncoder.encode(command.newPassword()));
        store.save(account);

        var user = account.getUser();

        return UserResult.from(user);
    }
}
