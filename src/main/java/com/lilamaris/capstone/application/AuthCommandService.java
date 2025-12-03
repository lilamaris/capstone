package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ConflictException;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.command.AuthCommand;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.AuthPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthCommandService implements AuthCommandUseCase {
    private final AuthPort authPort;
    private final UserPort userPort;

    @Override
    public AuthResult.Link linkAccount(AuthCommand.LinkOidc command) {
        var user = userPort.getById(command.userId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", command.userId())
        ));

        var account = Account.createOidc(command.provider(), command.providerId(), command.displayName());
        user = user.linkAccount(account);
        user = userPort.save(user);

        return AuthResult.Link.from(user, account);
    }

    @Override
    public AuthResult.Login createOrLogin(AuthCommand.CreateOrLoginOidc command) {
        User user;
        Account account;
        boolean isCreated = false;
        var accountOptional = authPort.getBy(command.provider(), command.providerId());

        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            user = userPort.getById(account.userId()).orElseThrow(() -> new ResourceNotFoundException(
                    String.format("User with id '%s' not found.", account.userId().value())
            ));
        } else {
            account = Account.createOidc(command.provider(), command.providerId(), command.displayName());
            user = User.create(command.displayName(), Role.USER);
            user = user.linkAccount(account);
            user = userPort.save(user);
            isCreated = true;
        }

        return AuthResult.Login.from(isCreated, user, account);
    }

    @Override
    public AuthResult.Login credentialLogin(String email, Function<String, Boolean> challengeFunction) {
        var account = authPort.getBy(Provider.LOCAL, email).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Account with email '%s' not found.", email)
        ));

        if (!challengeFunction.apply(account.passwordHash())) {
            throw new ConflictException("Challenge failed.");
        }

        var userId = account.userId();
        var user = userPort.getById(userId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", userId.value())
        ));

        return AuthResult.Login.from(false, user, account);
    }

    @Override
    public AuthResult.Register credentialRegister(String email, String passwordHash, String displayName) {
        var account = Account.createCredential(displayName, email, passwordHash);
        var user = User.create(displayName, Role.USER);
        user = user.linkAccount(account);
        user = userPort.save(user);

        return AuthResult.Register.from(user, account);
    }

}
